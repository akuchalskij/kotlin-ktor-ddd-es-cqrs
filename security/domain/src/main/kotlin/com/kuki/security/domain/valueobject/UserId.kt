package com.kuki.security.domain.valueobject

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserId internal constructor(private val value: String) {

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromString(string: String) = UserId(UUID.fromString(string).toString())
    }
}
