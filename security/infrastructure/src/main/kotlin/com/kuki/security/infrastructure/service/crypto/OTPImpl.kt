package com.kuki.security.infrastructure.service.crypto

import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.service.cache.CacheInterface
import kotlin.random.Random

class OTPImpl(private val cache: CacheInterface) : OTP {
    override suspend fun generate(userId: UserId): String {
        val token = generateOTP()

        cache.set("$userId$CACHE_PREFIX", token)

        return token
    }

    override suspend fun verify(userId: UserId, otp: String): Boolean {
        val token = cache.get<String>("$userId$CACHE_PREFIX")

        token?.let {
            cache.remove("$userId$CACHE_PREFIX")

            return token == otp
        }

        return false
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
        const val CACHE_PREFIX = "_otp"
    }
}
