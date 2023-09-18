package com.kuki.security.domain.valueobject

import com.kuki.security.domain.service.crypto.PasswordEncryption
import kotlinx.serialization.Serializable

@Serializable
data class HashedPassword internal constructor(private val hashedPassword: String) {

    fun isMatch(plainPassword: String, encryption: PasswordEncryption): Boolean {
        return encryption.isMatch(plainPassword, hashedPassword)
    }

    override fun toString(): String {
        return hashedPassword
    }

    companion object {
        fun encode(plainPassword: String, encryption: PasswordEncryption): HashedPassword =
            HashedPassword(encryption.encrypt(plainPassword))

        fun fromHash(hashedPassword: String): HashedPassword = HashedPassword(hashedPassword)
    }
}
