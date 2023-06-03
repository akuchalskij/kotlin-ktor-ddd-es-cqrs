package com.kuki.security.infrastructure.projector

import com.kuki.framework.projector.Repository
import com.kuki.security.domain.valueobject.Email

interface UserViewRepository : Repository<UserView, String> {

    suspend fun findByEmail(email: Email): UserView?
}
