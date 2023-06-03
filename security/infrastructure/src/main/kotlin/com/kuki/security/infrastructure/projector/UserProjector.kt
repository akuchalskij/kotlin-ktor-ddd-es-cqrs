package com.kuki.security.infrastructure.projector

import com.kuki.security.application.query.api.FindAllUsersQuery
import com.kuki.security.application.query.api.FindUserByIdQuery
import com.kuki.security.application.query.api.GetTokenQuery
import com.kuki.security.domain.event.UserEmailChanged
import com.kuki.security.domain.event.UserWasCreated
import com.kuki.security.domain.service.crypto.TokenGenerator
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.kuki.framework.projector.EventHandler
import com.kuki.framework.projector.Projector
import com.kuki.framework.queryhandling.QueryHandler
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class UserProjector(
    private val repository: UserViewRepository,
    private val tokenGenerator: TokenGenerator,
) : Projector() {

    @QueryHandler
    suspend fun ask(query: FindUserByIdQuery): UserView {
        return repository.findById(query.userId)
    }

    @QueryHandler
    suspend fun ask(query: FindAllUsersQuery): List<UserView> {
        return repository.findAll(query.limit, query.offset)
    }

    @QueryHandler
    suspend fun ask(query: GetTokenQuery): String {
        val user = repository.findByEmail(email = query.email) ?: error("User not found.")

        return tokenGenerator.generate(
            UserId.fromString(user.id),
            Email.fromString(user.email),
        )
    }

    @EventHandler
    suspend fun handle(event: UserWasCreated) {
        val view = UserView(
            id = event.id.toString(),
            email = event.email.toString(),
            password = event.password.toString(),
            isEmailVerified = false,
            firstName = null,
            lastName = null,
            createdAt = event.createdAt.toLocalDateTime(TimeZone.UTC),
            updatedAt = null
        )

        repository.save(view)
    }

    @EventHandler
    suspend fun handle(event: UserEmailChanged) {
        val user = repository.findById(event.userId.toString()).copy(
            email = event.email.toString()
        )

        repository.save(user)
    }
}
