package com.kuki.security.domain.service.crypto

interface PasswordEncryption {
    fun encrypt(password: String): String

    fun isMatch(password: String, encryptedPassword: String): Boolean
}
