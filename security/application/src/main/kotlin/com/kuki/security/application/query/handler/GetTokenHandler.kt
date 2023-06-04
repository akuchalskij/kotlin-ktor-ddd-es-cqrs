package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.GetTokenQuery
import com.kuki.security.domain.service.crypto.TokenGenerator
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.projector.UserViewRepository

class GetTokenHandler(
    private val userViewRepository: UserViewRepository,
    private val tokenGenerator: TokenGenerator,
) : QueryHandler<GetTokenQuery, String> {
    override suspend fun ask(query: GetTokenQuery): String {
        val user = userViewRepository.findByEmail(email = query.email) ?: error("User not found.")

        return tokenGenerator.generate(
            UserId.fromString(user.id),
            Email.fromString(user.email),
        )
    }
}
