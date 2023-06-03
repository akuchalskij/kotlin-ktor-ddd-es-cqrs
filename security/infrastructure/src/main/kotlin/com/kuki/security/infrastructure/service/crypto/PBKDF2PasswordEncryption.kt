package com.kuki.security.infrastructure.service.crypto

import com.kuki.security.domain.service.crypto.PasswordEncryption
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val SALT_SIZE = 16
private const val KEY_LENGTH = 128
private const val ITERATION_COUNT = 120_000

object PBKDF2PasswordEncryption : PasswordEncryption {

    override fun encrypt(password: String): String {
        val salt = generateSalt()
        return (salt + password.hashWithSalt(salt)).toBase64()
    }

    override fun isMatch(password: String, encryptedPassword: String): Boolean {
        val decoded = encryptedPassword.fromBase64()
        val salt = decoded.sliceArray(0 until SALT_SIZE)

        return password.hashWithSalt(salt).contentEquals(decoded.sliceArray(SALT_SIZE until decoded.size))
    }

    private fun generateSalt(): ByteArray {
        return ByteArray(SALT_SIZE).apply { SecureRandom().nextBytes(this) }
    }

    private fun String.hashWithSalt(salt: ByteArray): ByteArray {
        val keySpec: KeySpec = PBEKeySpec(this.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec).encoded
    }

    private fun ByteArray.toBase64() = Base64.getEncoder().encodeToString(this)

    private fun String.fromBase64() = Base64.getDecoder().decode(this)
}
