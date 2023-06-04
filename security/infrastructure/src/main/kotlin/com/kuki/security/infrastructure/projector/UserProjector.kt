package com.kuki.security.infrastructure.projector

import com.kuki.framework.projector.EventHandler
import com.kuki.framework.projector.Projector
import com.kuki.security.domain.event.UserEmailChanged
import com.kuki.security.domain.event.UserWasCreated
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class UserProjector(
    private val repository: UserViewRepository,
) : Projector() {

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
