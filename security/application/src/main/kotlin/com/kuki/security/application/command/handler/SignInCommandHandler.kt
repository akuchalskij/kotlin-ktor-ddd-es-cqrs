package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.SignInCommand
import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.PasswordEncryption

class SignInCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val checkUserByEmailInterface: CheckUserByEmailInterface,
    private val passwordEncryption: PasswordEncryption
) : CommandHandler<SignInCommand> {

    override suspend fun execute(command: SignInCommand) {
        val userId = checkUserByEmailInterface.existsByEmail(command.email)
            ?: throw InvalidCredentialsException("Invalid credentials entered")

        val user = userRepository.findById(userId)

        user.signIn(command.password, passwordEncryption)

        userRepository.store(user)
    }
}
