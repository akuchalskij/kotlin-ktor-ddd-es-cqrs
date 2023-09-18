package com.kuki.security.infrastructure.projector.kmongo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDocument(
    val id: String,
    val userId: String,
    val token: String,
    val expireAt: LocalDateTime,
)
