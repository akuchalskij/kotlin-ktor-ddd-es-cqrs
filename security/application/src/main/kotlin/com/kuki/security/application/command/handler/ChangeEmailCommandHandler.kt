package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.ChangeEmailCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface

class ChangeEmailCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val uniqueEmailSpecification: UniqueEmailSpecificationInterface,
    private val passwordEncryption: PasswordEncryption,
) : CommandHandler<ChangeEmailCommand> {

    override suspend fun execute(command: ChangeEmailCommand) {
        val user = userRepository.findById(command.userId)

        user.changeEmail(command.currentPassword, command.email, uniqueEmailSpecification, passwordEncryption)

        userRepository.store(user)
    }
}
