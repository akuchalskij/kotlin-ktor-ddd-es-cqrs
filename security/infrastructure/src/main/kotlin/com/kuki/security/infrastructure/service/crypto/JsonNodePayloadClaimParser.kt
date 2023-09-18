package com.kuki.security.infrastructure.service.crypto

import com.auth0.jwt.interfaces.Claim
import com.kuki.security.domain.service.crypto.PayloadClaimParser

class JsonNodePayloadClaimParser : PayloadClaimParser {
    override fun parse(data: Any?): Any? {
        return parse(data as Claim)
    }

    private fun parse(data: Claim): Any? {
        return when {
            data.isNull -> null
            data.isMissing -> null
            else -> data.`as`(Any::class.java)
        }
    }
}
