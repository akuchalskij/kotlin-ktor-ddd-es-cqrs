package com.kuki.security.domain.repository

import com.kuki.security.domain.User
import com.kuki.security.domain.valueobject.UserId

interface UserRepositoryInterface {

    suspend fun findById(id: UserId): User

    suspend fun store(user: User)
}
