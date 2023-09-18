package com.kuki.security.application.command.handler

import com.kuki.framework.commandhandling.CommandHandler
import com.kuki.security.application.command.api.SendActivationRequestCommand
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.service.sender.ActivationTokenSender

class SendActivationRequestCommandHandler(
    private val userRepository: UserRepositoryInterface,
    private val activationTokenSender: ActivationTokenSender,
    private val otp: OTP,
) : CommandHandler<SendActivationRequestCommand> {

    override suspend fun execute(command: SendActivationRequestCommand) {
        val user = userRepository.findById(command.userId)

        user.sendActivationRequest(command.email, activationTokenSender, otp)

        userRepository.store(user)
    }
}
