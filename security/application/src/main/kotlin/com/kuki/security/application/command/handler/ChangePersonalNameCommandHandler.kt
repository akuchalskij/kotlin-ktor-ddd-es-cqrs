package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ChangePersonalNameCommand
import com.kuki.security.domain.repository.UserRepositoryInterface

class ChangePersonalNameCommandHandler(
    private val userRepository: UserRepositoryInterface,
) : CommandHandler<ChangePersonalNameCommand> {

    override suspend fun execute(command: ChangePersonalNameCommand) {
        val user = userRepository.findById(command.userId)

        user.changePersonalName(command.personalName)

        userRepository.store(user)
    }
}
