package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.CreateUserCommand
import com.kuki.security.domain.User
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface

class CreateUserCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val uniqueEmailSpecification: UniqueEmailSpecificationInterface
) : CommandHandler<CreateUserCommand> {

    override suspend fun execute(command: CreateUserCommand) {
        val user = User.create(command.userId, command.email, command.password, uniqueEmailSpecification)

        userRepository.store(user)
    }
}

