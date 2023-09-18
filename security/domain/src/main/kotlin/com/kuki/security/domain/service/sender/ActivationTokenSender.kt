package com.kuki.security.domain.service.sender

import com.kuki.security.domain.valueobject.Email

/**
 * Service to send activation tokens.
 */
interface ActivationTokenSender {

    suspend fun send(email: Email, token: String)
}
