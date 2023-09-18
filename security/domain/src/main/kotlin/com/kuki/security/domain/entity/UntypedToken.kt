package com.kuki.security.domain.entity

import com.kuki.security.domain.service.crypto.TokenBackend
import kotlin.time.Duration.Companion.seconds

class UntypedToken(
    tokenBackend: TokenBackend, token: String? = null
) : Token(tokenBackend, token, "untyped", 0.seconds) {

    /**
     * Untyped tokens do not verify the "token_type" claim.
     * This is useful when performing general validation of a token's signature and other
     * properties which do not relate to the token's intended use.
     */
    override fun verifyTokenType() {
    }
}
