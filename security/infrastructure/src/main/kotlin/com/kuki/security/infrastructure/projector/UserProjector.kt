package com.kuki.security.infrastructure.projector

import com.kuki.framework.projector.EventHandler
import com.kuki.framework.projector.Projector
import com.kuki.security.domain.event.UserCreated
import com.kuki.security.domain.event.UserResetPasswordConfirmed
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class UserProjector(
    private val repository: UserViewRepository,
) : Projector() {

    @EventHandler
    suspend fun handle(event: UserCreated) {
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
    suspend fun handle(event: UserResetPasswordConfirmed) {
        val user = repository.findById(event.userId.toString()).copy(
            password = event.newPassword.toString()
        )

        repository.save(user)
    }
}
