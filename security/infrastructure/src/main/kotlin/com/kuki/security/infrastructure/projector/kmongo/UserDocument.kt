package com.kuki.security.infrastructure.projector.kmongo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDocument(
    val id: String,
    val email: String,
    val password: String,
    val isEmailVerified: Boolean,
    val firstName: String?,
    val lastName: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
