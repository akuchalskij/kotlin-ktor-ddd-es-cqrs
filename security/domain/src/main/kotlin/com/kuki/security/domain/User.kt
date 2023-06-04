package com.kuki.security.domain

import com.kuki.framework.domain.AggregateId
import com.kuki.framework.domain.AggregateRoot
import com.kuki.framework.eventsourcing.EventSourcingHandler
import com.kuki.security.domain.event.UserEmailChanged
import com.kuki.security.domain.event.UserPasswordChanged
import com.kuki.security.domain.event.UserSignedIn
import com.kuki.security.domain.event.UserWasCreated
import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class User : AggregateRoot() {

    @AggregateId
    private lateinit var id: UserId

    private lateinit var email: Email

    private lateinit var password: HashedPassword

    private var isEmailVerified: Boolean = false

    private var firstName: String? = null

    private var lastName: String? = null

    private var createdAt: Instant? = null

    private var updatedAt: Instant? = null

    suspend fun changeEmail(email: Email, uniqueEmailSpecification: UniqueEmailSpecificationInterface) {
        check(this.email != email) {
            "New email should be different"
        }

        uniqueEmailSpecification.isUnique(email.toString())

        apply(UserEmailChanged(userId = id, email = email, updatedAt = Clock.System.now()))
    }

    fun signIn(plainPassword: String, passwordEncryption: PasswordEncryption) {
        if (password.isMatch(plainPassword, passwordEncryption)) {
            apply(UserSignedIn(userId = id, email = email, signedInAt = Clock.System.now()))

            return
        }

        throw InvalidCredentialsException("Invalid credentials")
    }

    fun changePassword(newPassword: HashedPassword) {
        check(this.password != newPassword) {
            "New password should be different"
        }

        apply(UserPasswordChanged(userId = id, newPassword = newPassword, updatedAt = Clock.System.now()))
    }

    @EventSourcingHandler
    private fun on(event: UserWasCreated) {
        id = event.id
        email = event.email
        password = event.password
        createdAt = event.createdAt
    }

    @EventSourcingHandler
    private fun on(event: UserEmailChanged) {
        email = event.email
        updatedAt = event.updatedAt
    }

    companion object {

        suspend fun create(
            id: UserId,
            email: Email,
            password: HashedPassword,
            uniqueEmailSpecification: UniqueEmailSpecificationInterface,
        ): User {
            // TODO: Create a factory for aggregate
            val user = User().apply {
                this.id = id
            }

            uniqueEmailSpecification.isUnique(email.toString())

            user.apply(UserWasCreated(id = id, email = email, password = password, createdAt = Clock.System.now()))

            return user
        }
    }
}
