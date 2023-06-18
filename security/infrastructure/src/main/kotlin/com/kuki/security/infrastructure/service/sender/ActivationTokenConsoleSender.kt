package com.kuki.security.infrastructure.service.sender

import com.kuki.security.domain.service.sender.ActivationTokenSender
import com.kuki.security.domain.valueobject.Email
import org.slf4j.LoggerFactory

class ActivationTokenConsoleSender : ActivationTokenSender {
    override suspend fun send(email: Email, token: String) {
        logger.info("Token for $email: $token")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ActivationTokenConsoleSender::class.java)
    }
}
