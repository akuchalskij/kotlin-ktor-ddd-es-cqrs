package com.kuki.security.infrastructure.projector.inmemory

import com.kuki.framework.projector.ProjectionException
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.infrastructure.projector.UserViewRepository

class InMemoryUserViewRepository : UserViewRepository, CheckUserByEmailInterface {

    private val table: HashMap<String, UserView> = hashMapOf()

    override suspend fun existsByEmail(email: Email): UserId? {
        return table.values
            .find { view -> view.email == email.toString() }
            ?.let { view -> UserId.fromString(view.id) }
    }

    override suspend fun findByEmail(email: Email): UserView? {
        return table.values
            .find { view -> view.email == email.toString() }
    }

    override suspend fun save(data: UserView) {
        table[data.id] = data
    }

    override suspend fun findById(id: String): UserView {
        return table[id] ?: throw ProjectionException.ProjectionNotFound("User with $id not found.")
    }

    override suspend fun findAll(): List<UserView> {
        return table.values.toList()
    }

    override suspend fun findAll(limit: Long, offset: Long): List<UserView> {
        return table.values.toList().slice(offset.toInt() until limit.toInt())
    }
}
