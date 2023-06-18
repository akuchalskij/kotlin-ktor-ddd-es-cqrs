package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ActivateUserCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.OTP

class ActivateUserCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val otp: OTP,
) : CommandHandler<ActivateUserCommand> {

    override suspend fun execute(command: ActivateUserCommand) {
        val user = userRepository.findById(command.userId)

        user.activate(command.token, otp)

        userRepository.store(user)
    }
}
