package com.kuki.security.domain.valueobject

import kotlinx.serialization.Serializable

@Serializable
data class Tokens internal constructor(
    val accessToken: String,
    val refreshToken: String,
    val type: String,
) {
    companion object {
        fun bearerFromTokenPairs(accessToken: String, refreshToken: String) =
            Tokens(accessToken, refreshToken, "Bearer")
    }
}
