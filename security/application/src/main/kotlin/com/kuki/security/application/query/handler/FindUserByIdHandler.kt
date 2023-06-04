package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.FindUserByIdQuery
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.infrastructure.projector.UserViewRepository

class FindUserByIdHandler(
    private val userViewRepository: UserViewRepository,
) : QueryHandler<FindUserByIdQuery, UserView> {

    override suspend fun ask(query: FindUserByIdQuery): UserView {
        return userViewRepository.findById(query.userId)
    }
}
