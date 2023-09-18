package com.kuki.security.infrastructure.service.sender

import com.kuki.security.domain.service.sender.ResetPasswordConfirmationSender
import com.kuki.security.domain.valueobject.Email
import org.slf4j.LoggerFactory

class ResetPasswordConfirmationConsoleSender : ResetPasswordConfirmationSender {
    override suspend fun send(email: Email, token: String) {
        logger.info("Token for $email: $token")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ResetPasswordConfirmationConsoleSender::class.java)
    }
}
