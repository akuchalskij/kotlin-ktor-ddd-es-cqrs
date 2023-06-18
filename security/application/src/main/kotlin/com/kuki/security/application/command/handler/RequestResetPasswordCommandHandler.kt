package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.RequestResetPasswordCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.service.sender.ResetPasswordConfirmationSender

class RequestResetPasswordCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val resetPasswordConfirmationSender: ResetPasswordConfirmationSender,
    private val otp: OTP,
) : CommandHandler<RequestResetPasswordCommand> {

    override suspend fun execute(command: RequestResetPasswordCommand) {
        val user = userRepository.findById(command.userId)

        user.requestResetPassword(command.email, resetPasswordConfirmationSender, otp)

        userRepository.store(user)
    }
}
