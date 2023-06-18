package com.kuki.security.application.koin

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.command.handler.*
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
            passwordEncryption = get(),
        )
    } bind CommandHandler::class

    single {
        ChangePasswordCommandHandler(
            userRepository = get(),
            passwordEncryption = get(),
        )
    } bind CommandHandler::class

    single {
        ActivateUserCommandHandler(
            userRepository = get(),
            otp = get()
        )
    } bind CommandHandler::class

    single {
        ChangePersonalNameCommandHandler(
            userRepository = get()
        )
    } bind CommandHandler::class

    single {
        ConfirmResetPasswordCommandHandler(
            userRepository = get(),
            otp = get()
        )
    } bind CommandHandler::class

    single {
        DeleteUserCommandHandler(
            userRepository = get()
        )
    } bind CommandHandler::class

    single {
        RequestResetPasswordCommandHandler(
            userRepository = get(),
            resetPasswordConfirmationSender = get(),
            otp = get()
        )
    } bind CommandHandler::class

    single {
        SendActivationRequestCommandHandler(
            userRepository = get(),
            activationTokenSender = get(),
            otp = get()
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
