package com.kuki.security.application.koin

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.command.handler.ChangeEmailCommandHandler
import com.kuki.security.application.command.handler.ChangePasswordCommandHandler
import com.kuki.security.application.command.handler.CreateUserCommandHandler
import com.kuki.security.application.command.handler.SignInCommandHandler
import com.kuki.security.application.query.handler.FindAllUsersHandler
import com.kuki.security.application.query.handler.FindUserByIdHandler
import com.kuki.security.application.query.handler.GetTokenHandler
import org.koin.dsl.bind
import org.koin.dsl.module

val applicationModule = module {
    single {
        CreateUserCommandHandler(
            userRepository = get(),
            uniqueEmailSpecification = get(),
        )
    } bind CommandHandler::class

    single {
        SignInCommandHandler(
            userRepository = get(),
            checkUserByEmailInterface = get(),
            passwordEncryption = get()
        )
    } bind CommandHandler::class

    single {
        ChangeEmailCommandHandler(
            userRepository = get(),
            uniqueEmailSpecification = get(),
        )
    } bind CommandHandler::class

    single {
        ChangePasswordCommandHandler(
            userRepository = get()
        )
    } bind CommandHandler::class

    single {
        FindAllUsersHandler(userViewRepository = get())
    } bind QueryHandler::class

    single {
        FindUserByIdHandler(userViewRepository = get())
    } bind QueryHandler::class

    single {
        GetTokenHandler(userViewRepository = get(), tokenGenerator = get())
    } bind QueryHandler::class
}
