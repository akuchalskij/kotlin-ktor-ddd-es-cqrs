package com.kuki.security.domain.service.sender

import com.kuki.security.domain.valueobject.Email

/**
 * Service for sending reset password confirmation
 */
interface ResetPasswordConfirmationSender {

    suspend fun send(email: Email, token: String)
}
