package com.kuki.security.ui.http.services

import com.kuki.security.domain.service.crypto.TokenGenerator
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.Instant
import java.util.*

class JWTTokenGenerator(
    private val audience: String,
    private val issuer: String,
    private val secret: String,
) : TokenGenerator {

    override suspend fun generate(userId: UserId, email: Email): String {
        return JWT.create()
            .withSubject(userId.toString())
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email.toString())
            .withExpiresAt(Date.from(Instant.now().plusMillis(60000)))
            .sign(Algorithm.HMAC256(secret))
    }
}
