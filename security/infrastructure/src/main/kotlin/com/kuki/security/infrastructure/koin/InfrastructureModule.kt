package com.kuki.security.infrastructure.koin

import com.kuki.framework.commandhandling.Command
import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.framework.commandhandling.SimpleCommandBus
import com.kuki.framework.eventhandling.EventBus
import com.kuki.framework.eventhandling.EventListener
import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventstore.EventStore
import com.kuki.framework.queryhandling.Query
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.framework.queryhandling.SimpleQueryBus
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.service.sender.ActivationTokenSender
import com.kuki.security.domain.service.sender.ResetPasswordConfirmationSender
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.infrastructure.projector.UserProjector
import com.kuki.security.infrastructure.projector.UserViewRepository
import com.kuki.security.infrastructure.projector.exposed.ExposedUserViewRepository
import com.kuki.security.infrastructure.repository.UserEventStore
import com.kuki.security.infrastructure.service.crypto.InMemoryOTPStorage
import com.kuki.security.infrastructure.service.crypto.OTPImpl
import com.kuki.security.infrastructure.service.crypto.OTPStorage
import com.kuki.security.infrastructure.service.crypto.PBKDF2PasswordEncryption
import com.kuki.security.infrastructure.service.sender.ActivationTokenConsoleSender
import com.kuki.security.infrastructure.service.sender.ResetPasswordConfirmationConsoleSender
import com.kuki.security.infrastructure.specification.UniqueEmailSpecification
import com.kuki.shared.infrastructure.eventstore.exposed.ExposedEventStore
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val infrastructureModule = module {
    single { ExposedUserViewRepository() } binds arrayOf(
        UserViewRepository::class,
        CheckUserByEmailInterface::class,
    )

    single { UserProjector(repository = get()) } bind EventListener::class

    single<CommandBus> {
        val commandHandlers by lazy { getAll<CommandHandler<*>>() }

        val commandBus = SimpleCommandBus()

        commandHandlers.forEach { listener ->
            logger.info("Subscribe command listener [$listener]")
            @Suppress("UNCHECKED_CAST")
            commandBus.subscribe(listener as CommandHandler<Command>)
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
        val queryHandlers by lazy { getAll<QueryHandler<Query, *>>() }

        val queryBus = SimpleQueryBus()

        queryHandlers.forEach { listener ->
            logger.info("Subscribe query listener [$listener]")
            queryBus.subscribe(listener)
        }

        return@single queryBus
    }

    single<PasswordEncryption> { PBKDF2PasswordEncryption }

    single { UserEventStore(eventBus = get(), eventStore = get()) } bind UserRepositoryInterface::class

    single<UniqueEmailSpecificationInterface> { UniqueEmailSpecification(checkUserByEmailInterface = get()) }

    single { InMemoryOTPStorage() } bind OTPStorage::class

    single<OTP> { OTPImpl(storage = get()) }

    single<ActivationTokenSender> { ActivationTokenConsoleSender() }
    single<ResetPasswordConfirmationSender> { ResetPasswordConfirmationConsoleSender() }
}
