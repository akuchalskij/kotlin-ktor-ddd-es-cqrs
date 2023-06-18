package com.kuki.security.domain.service.crypto

import com.kuki.security.domain.valueobject.UserId

/**
 * OTP Service.
 *
 * Provides methods to generate and verify one time password.
 */
interface OTP {

    suspend fun generate(userId: UserId): String

    suspend fun verify(userId: UserId, otp: String): Boolean
}
