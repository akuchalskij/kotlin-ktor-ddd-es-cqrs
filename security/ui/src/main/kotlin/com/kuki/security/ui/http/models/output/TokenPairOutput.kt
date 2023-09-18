package com.kuki.security.ui.http.models.output

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairOutput(
    val accessToken: String,
    val refreshToken: String,
)
