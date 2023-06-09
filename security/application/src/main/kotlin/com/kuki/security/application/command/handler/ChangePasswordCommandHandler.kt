package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ChangePasswordCommand
import com.kuki.security.domain.repository.UserRepositoryInterface

class ChangePasswordCommandHandler(
    private val userRepository: UserRepositoryInterface
) : CommandHandler<ChangePasswordCommand> {

    override suspend fun execute(command: ChangePasswordCommand) {
        val user = userRepository.findById(command.userId)

        user.changePassword(command.password)

        userRepository.store(user)
    }
}
