package com.kuki.security.infrastructure.service.crypto

import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.valueobject.UserId
import kotlin.random.Random

class OTPImpl(
    private val storage: OTPStorage<UserId, String>
) : OTP {
    override suspend fun generate(userId: UserId): String {
        val token = generateOTP()

        storage.set(userId, token)

        return token
    }

    override suspend fun verify(userId: UserId, otp: String): Boolean {
        val token = storage.get(userId) ?: return false

        return token.isNotEmpty()
    }

    private fun generateOTP(): String {
        val random = Random(OTP_LENGTH)
        val otp = CharArray(OTP_LENGTH)

        for (i in 0 until OTP_LENGTH) {
            otp[i] = NUMBERS[random.nextInt(NUMBERS.length)]
        }

        return String(otp)
    }

    companion object {
        const val OTP_LENGTH = 6
        const val NUMBERS = "1234567890"
    }
}
