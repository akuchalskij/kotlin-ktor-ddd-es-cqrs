package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ConfirmResetPasswordCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.OTP

class ConfirmResetPasswordCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val otp: OTP,
) : CommandHandler<ConfirmResetPasswordCommand> {

    override suspend fun execute(command: ConfirmResetPasswordCommand) {
        val user = userRepository.findById(command.userId)

        user.confirmResetPassword(command.token, command.newPassword, otp)

        userRepository.store(user)
    }
}
