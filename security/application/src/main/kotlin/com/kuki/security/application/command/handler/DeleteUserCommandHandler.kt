package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.DeleteUserCommand
import com.kuki.security.domain.repository.UserRepositoryInterface

class DeleteUserCommandHandler(
    private val userRepository: UserRepositoryInterface,
) : CommandHandler<DeleteUserCommand> {

    override suspend fun execute(command: DeleteUserCommand) {
        val user = userRepository.findById(command.userId)

        user.delete()

        userRepository.store(user)
    }
}
