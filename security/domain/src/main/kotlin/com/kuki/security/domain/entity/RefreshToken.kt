package com.kuki.security.domain.entity

import com.kuki.security.domain.service.crypto.TokenBackend
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class RefreshToken(
    tokenBackend: TokenBackend, token: String? = null
) : Token(tokenBackend, token, "refresh", 1.days) {

    private val noCopyClaims = listOf(
        TOKEN_TYPE_CLAIM, EXP_CLAIM, JTI_CLAIM
    )

    val accessToken
        get(): AccessToken {
            val currentTime = Clock.System.now()
            val accessToken = AccessToken(tokenBackend)

            accessToken.setExpiration(currentTime)

            for ((claim, value) in payload.entries) {
                if (claim in noCopyClaims) {
                    continue
                }

                accessToken.setClaim(claim, value)
            }

            return accessToken
        }


    companion object {
        fun forUserId(tokenBackend: TokenBackend, userId: UserId): RefreshToken {
            val token = RefreshToken(tokenBackend)
            token.setUserId(userId)

            return token
        }
    }
}
