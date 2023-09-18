package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.GetNewAccessTokenQuery
import com.kuki.security.domain.entity.RefreshToken
import com.kuki.security.domain.service.crypto.TokenBackend
import com.kuki.security.domain.valueobject.Tokens

class GetNewAccessTokenHandler(
    private val tokenBackend: TokenBackend,
) : QueryHandler<GetNewAccessTokenQuery, Tokens> {

    override suspend fun ask(query: GetNewAccessTokenQuery): Tokens {
        val refreshToken = RefreshToken(tokenBackend, query.token).apply {
            setJti()
            setExpiration()
            setIat()
        }

        return Tokens.bearerFromTokenPairs(
            accessToken = refreshToken.accessToken.toString(),
            refreshToken = refreshToken.toString(),
        )
    }
}
