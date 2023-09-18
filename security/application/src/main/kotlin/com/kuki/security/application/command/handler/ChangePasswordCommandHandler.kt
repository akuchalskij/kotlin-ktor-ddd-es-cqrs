package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ChangePasswordCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.PasswordEncryption

class ChangePasswordCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val passwordEncryption: PasswordEncryption,
) : CommandHandler<ChangePasswordCommand> {

    override suspend fun execute(command: ChangePasswordCommand) {
        val user = userRepository.findById(command.userId)

        user.changePassword(command.currentPassword, command.password, passwordEncryption)

        userRepository.store(user)
    }
}
