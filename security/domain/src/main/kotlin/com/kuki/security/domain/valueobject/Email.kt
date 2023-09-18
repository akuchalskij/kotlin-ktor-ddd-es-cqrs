package com.kuki.security.domain.valueobject

import kotlinx.serialization.Serializable

@Serializable
data class Email internal constructor(private val email: String) {

    override fun toString(): String {
        return email
    }

    companion object {
        fun fromString(email: String): Email {
            val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

            require(emailRegex.matches(email)) {
                "Email is not valid"
            }

            return Email(email)
        }
    }
}
