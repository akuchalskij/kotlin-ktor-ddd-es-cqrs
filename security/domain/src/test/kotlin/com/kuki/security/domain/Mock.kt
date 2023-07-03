package com.kuki.security.domain

import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.service.sender.ActivationTokenSender
import com.kuki.security.domain.service.sender.ResetPasswordConfirmationSender
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId

object Mock {

    val passwordEncryption = object : PasswordEncryption {
        override fun encrypt(password: String): String {
            return password
        }

        override fun isMatch(password: String, encryptedPassword: String): Boolean {
            return password == encryptedPassword
        }
    }

    val activationTokenSender = object : ActivationTokenSender {
        override suspend fun send(email: Email, token: String) {

        }
    }

    val resetPasswordConfirmationSender = object : ResetPasswordConfirmationSender {
        override suspend fun send(email: Email, token: String) {

        }
    }

    fun otp(
        generate: suspend (userId: UserId) -> String = { "" },
        verify: suspend (userId: UserId, otp: String) -> Boolean = { _, _ -> true }
    ) = object : OTP {
        override suspend fun generate(userId: UserId): String {
            return generate(userId)
        }

        override suspend fun verify(userId: UserId, otp: String): Boolean {
            return verify(userId, otp)
        }

    }

    fun emailUniqueEmailSpecification(isUnique: Boolean = true) = object : UniqueEmailSpecificationInterface {
        override suspend fun isUnique(email: String): Boolean {
            return isUnique
        }
    }


}
