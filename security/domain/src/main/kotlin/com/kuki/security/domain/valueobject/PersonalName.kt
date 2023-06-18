package com.kuki.security.domain.valueobject

import kotlinx.serialization.Serializable

@Serializable
data class PersonalName internal constructor(
    private val givenName: String,
    private val middleName: String,
    private val surname: String,
) {

    fun givenName(): String {
        return givenName
    }

    fun middleName(): String {
        return middleName
    }

    fun surname(): String {
        return surname
    }

    fun fullName(): String {
        return "$givenName $middleName $surname".trimIndent()
    }

    override fun toString(): String {
        return fullName()
    }

    companion object {
        fun fromString(givenName: String, middleName: String, surname: String): PersonalName {
            return PersonalName(givenName, middleName, surname)
        }
    }
}
