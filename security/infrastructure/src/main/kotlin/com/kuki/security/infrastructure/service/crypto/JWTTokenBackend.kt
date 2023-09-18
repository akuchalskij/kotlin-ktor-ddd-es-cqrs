package com.kuki.security.infrastructure.service.crypto

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kuki.security.domain.service.crypto.PayloadClaimParser
import com.kuki.security.domain.service.crypto.TokenBackend

class JWTTokenBackend(
    private val audience: String,
    private val issuer: String,
    private val payloadClaimParser: PayloadClaimParser,
    secret: String,
) : TokenBackend {

    private val algorithm = Algorithm.HMAC256(secret)

    override fun encode(payload: Map<String, Any?>): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withPayload(payload)
        .sign(algorithm)

    override fun decode(rawToken: String): Map<String, Any?> = JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
        .verify(rawToken)
        .claims
        .mapValues { (_, value) ->
            payloadClaimParser.parse(value)
        }
}
