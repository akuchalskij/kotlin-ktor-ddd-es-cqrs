package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.GetVerifiedTokenQuery
import com.kuki.security.domain.entity.UntypedToken
import com.kuki.security.domain.service.crypto.TokenBackend

class GetVerifiedTokenHandler(
    private val tokenBackend: TokenBackend,
) : QueryHandler<GetVerifiedTokenQuery, String> {

    override suspend fun ask(query: GetVerifiedTokenQuery): String {
        val token = UntypedToken(tokenBackend, query.token)

        return token.toString()
    }
}
