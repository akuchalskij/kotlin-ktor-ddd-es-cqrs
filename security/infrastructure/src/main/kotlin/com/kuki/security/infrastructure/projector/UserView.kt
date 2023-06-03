package com.kuki.security.infrastructure.projector

import kotlinx.datetime.LocalDateTime

data class UserView(
    val id: String,
    val email: String,
    val password: String,
    val isEmailVerified: Boolean,
    val firstName: String?,
    val lastName: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
