package com.kuki.security.application.query.handler

import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.security.application.query.api.FindAllUsersQuery
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.infrastructure.projector.UserViewRepository

class FindAllUsersHandler(
    private val userViewRepository: UserViewRepository,
) : QueryHandler<FindAllUsersQuery, List<UserView>> {

    override suspend fun ask(query: FindAllUsersQuery): List<UserView> {
        return userViewRepository.findAll(query.limit, query.offset)
    }
}
