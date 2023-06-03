package com.kuki.security.infrastructure.koin

import com.kuki.framework.commandhandling.Command
import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.commandhandling.CommandListener
import com.kuki.framework.commandhandling.SimpleCommandBus
import com.kuki.framework.domain.Event
import com.kuki.framework.eventhandling.EventBus
import com.kuki.framework.eventhandling.EventListener
import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventstore.EventStore
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.framework.queryhandling.QueryListener
import com.kuki.framework.queryhandling.SimpleQueryBus
import com.kuki.security.application.command.handler.ChangeEmailCommandHandler
import com.kuki.security.application.command.handler.CreateUserCommandHandler
import com.kuki.security.application.command.handler.SignInCommandHandler
import com.kuki.security.domain.event.UserEmailChanged
import com.kuki.security.domain.event.UserSignedIn
import com.kuki.security.domain.event.UserWasCreated
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.infrastructure.eventstore.exposed.ExposedEventStore
import com.kuki.security.infrastructure.projector.UserProjector
import com.kuki.security.infrastructure.projector.UserViewRepository
import com.kuki.security.infrastructure.projector.exposed.ExposedUserViewRepository
import com.kuki.security.infrastructure.repository.UserEventStore
import com.kuki.security.infrastructure.service.crypto.PBKDF2PasswordEncryption
import com.kuki.security.infrastructure.specification.UniqueEmailSpecification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

@OptIn(DelicateCoroutinesApi::class)
val infrastructureModule = module {
    single { ExposedUserViewRepository() } binds arrayOf(
        UserViewRepository::class,
        CheckUserByEmailInterface::class,
    )

    single { UserProjector(repository = get(), tokenGenerator = get()) } binds arrayOf(
        EventListener::class,
        QueryListener::class
    )

    single {
        CreateUserCommandHandler(
            userRepository = get(),
            uniqueEmailSpecification = get(),
        )
    } bind CommandListener::class

    single {
        SignInCommandHandler(
            userRepository = get(),
            checkUserByEmailInterface = get(),
            passwordEncryption = get()
        )
    } bind CommandListener::class

    single {
        ChangeEmailCommandHandler(
            userRepository = get(),
            uniqueEmailSpecification = get(),
        )
    } bind CommandListener::class

    single<CommandBus> {
        val commandListeners by lazy { getAll<CommandListener<*>>() }

        val commandBus = SimpleCommandBus()

        commandListeners.forEach { listener ->
            logger.info("Subscribe command listener [$listener]")
            @Suppress("UNCHECKED_CAST")
            commandBus.subscribe(listener as CommandListener<Command>)
        }

        return@single commandBus
    }

    single<EventStore> {
        ExposedEventStore()
    }

    single<EventBus> {
        val eventListeners by lazy { getAll<EventListener>() }

        val eventBus = SimpleEventBus()

        eventListeners.forEach { listener ->
            logger.info("Subscribe event listener [$listener]")
            eventBus.subscribe(listener)
        }

        return@single eventBus
    }

    single<QueryBus> {
        val queryListeners by lazy { getAll<QueryListener>() }

        val queryBus = SimpleQueryBus()

        queryListeners.forEach { listener ->
            logger.info("Subscribe query listener [$listener]")
            queryBus.subscribe(listener)
        }

        return@single queryBus
    }

    single<PasswordEncryption> { PBKDF2PasswordEncryption }

    single { UserEventStore(eventBus = get(), eventStore = get()) } bind UserRepositoryInterface::class

    single<UniqueEmailSpecificationInterface> { UniqueEmailSpecification(get()) }
}

val jsonSerializer = Json {
    serializersModule = SerializersModule {
        polymorphic(Event::class) {
            subclass(UserWasCreated::class)
            subclass(UserEmailChanged::class)
            subclass(UserSignedIn::class)
        }
    }
}
