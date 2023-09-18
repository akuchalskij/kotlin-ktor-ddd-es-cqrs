package com.kuki.security.infrastructure.projector

import com.kuki.framework.projector.EventHandler
import com.kuki.framework.projector.Projector
import com.kuki.security.domain.event.*
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
            givenName = null,
            middleName = null,
            surname = null,
            createdAt = event.createdAt.toLocalDateTime(TimeZone.UTC),
            updatedAt = null,
            deletedAt = null
        )

        repository.save(view)
    }

    @EventHandler
    suspend fun handle(event: UserEmailChanged) {
        val user = repository.findById(event.userId.toString()).copy(
            email = event.email.toString(),
            updatedAt = event.updatedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }

    @EventHandler
    suspend fun handle(event: UserPasswordChanged) {
        val user = repository.findById(event.userId.toString()).copy(
            password = event.newPassword.toString(),
            updatedAt = event.updatedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }

    @EventHandler
    suspend fun handle(event: UserResetPasswordConfirmed) {
        val user = repository.findById(event.userId.toString()).copy(
            password = event.newPassword.toString(),
            updatedAt = event.updatedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }

    @EventHandler
    suspend fun handle(event: UserActivated) {
        val user = repository.findById(event.userId.toString()).copy(
            isEmailVerified = true,
            updatedAt = event.activatedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }

    @EventHandler
    suspend fun handle(event: UserPersonalNameChanged) {
        val user = repository.findById(event.userId.toString()).copy(
            givenName = event.personalName.givenName(),
            middleName = event.personalName.middleName(),
            surname = event.personalName.surname(),
            updatedAt = event.updatedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }

    @EventHandler
    suspend fun handle(event: UserDeleted) {
        val user = repository.findById(event.userId.toString()).copy(
            deletedAt = event.deletedAt.toLocalDateTime(TimeZone.UTC)
        )

        repository.save(user)
    }
}
