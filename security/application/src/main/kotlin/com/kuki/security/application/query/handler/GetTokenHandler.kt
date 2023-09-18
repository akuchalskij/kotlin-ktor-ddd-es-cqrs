package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.GetTokenQuery
import com.kuki.security.domain.entity.RefreshToken
import com.kuki.security.domain.service.crypto.TokenBackend
import com.kuki.security.domain.valueobject.Tokens
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.projector.UserViewRepository

class GetTokenHandler(
    private val userViewRepository: UserViewRepository,
    private val tokenBackend: TokenBackend,
) : QueryHandler<GetTokenQuery, Tokens> {

    override suspend fun ask(query: GetTokenQuery): Tokens {
        val user = userViewRepository.findByEmail(email = query.email) ?: error("User not found.")
        val refreshToken = RefreshToken.forUserId(tokenBackend, UserId.fromString(user.id))
        return Tokens.bearerFromTokenPairs(
            accessToken = refreshToken.accessToken.toString(),
            refreshToken = refreshToken.toString(),
        )
    }
}
