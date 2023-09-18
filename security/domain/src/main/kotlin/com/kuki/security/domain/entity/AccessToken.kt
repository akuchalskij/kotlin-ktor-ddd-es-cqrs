package com.kuki.security.domain.entity

import com.kuki.security.domain.service.crypto.TokenBackend
import kotlin.time.Duration.Companion.minutes

class AccessToken(
    tokenBackend: TokenBackend, token: String? = null
) : Token(tokenBackend, token, "access", 15.minutes)
