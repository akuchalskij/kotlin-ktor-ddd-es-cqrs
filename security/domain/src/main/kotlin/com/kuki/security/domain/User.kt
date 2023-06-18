package com.kuki.security.domain

import com.kuki.framework.domain.AggregateId
import com.kuki.framework.domain.AggregateRoot
import com.kuki.framework.eventsourcing.EventSourcingHandler
import com.kuki.security.domain.event.*
import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.domain.service.crypto.OTP
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.service.sender.ActivationTokenSender
import com.kuki.security.domain.service.sender.ResetPasswordConfirmationSender
import com.kuki.security.domain.specification.UniqueEmailSpecificationInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.PersonalName
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class User : AggregateRoot() {

    @AggregateId
    private lateinit var id: UserId

    private lateinit var email: Email

    private lateinit var password: HashedPassword

    private var isEmailVerified: Boolean = false

    private var personalName: PersonalName? = null

    private var createdAt: Instant? = null

    private var updatedAt: Instant? = null

    private var deletedAt: Instant? = null

    fun signIn(plainPassword: String, passwordEncryption: PasswordEncryption) {
        check(deletedAt == null) {
            "User is deleted"
        }

        if (password.isMatch(plainPassword, passwordEncryption)) {
            apply(UserSignedIn(userId = id, email = email, signedInAt = Clock.System.now()))

            return
        }

        throw InvalidCredentialsException("Invalid credentials")
    }

    suspend fun sendActivationRequest(email: Email, activationTokenSender: ActivationTokenSender, otp: OTP) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(this.email == email) {
            "Email should be same as user's email"
        }

        val token = otp.generate(id)
        activationTokenSender.send(email, token)

        apply(UserActivationSent(userId = id, email = email, updatedAt = Clock.System.now()))
    }

    suspend fun activate(token: String, otp: OTP) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(otp.verify(id, token)) {
            "Invalid activation token"
        }

        apply(UserActivated(userId = id, activatedAt = Clock.System.now()))
    }

    suspend fun changeEmail(
        currentPassword: String,
        email: Email,
        uniqueEmailSpecification: UniqueEmailSpecificationInterface,
        passwordEncryption: PasswordEncryption
    ) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(password.isMatch(currentPassword, passwordEncryption)) {
            "Current password is not correct"
        }

        require(this.email != email) {
            "New email should be different"
        }

        uniqueEmailSpecification.isUnique(email.toString())

        apply(UserEmailChanged(userId = id, email = email, updatedAt = Clock.System.now()))
    }

    fun changePassword(currentPassword: String, newPassword: HashedPassword, passwordEncryption: PasswordEncryption) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(password.isMatch(currentPassword, passwordEncryption)) {
            "Current password is not correct"
        }

        require(this.password != newPassword) {
            "New password should be different"
        }

        apply(UserPasswordChanged(userId = id, newPassword = newPassword, updatedAt = Clock.System.now()))
    }

    suspend fun requestResetPassword(
        email: Email,
        resetPasswordConfirmationSender: ResetPasswordConfirmationSender,
        otp: OTP
    ) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(this.email == email) {
            "Email should be same as user's email"
        }

        val token = otp.generate(id)
        resetPasswordConfirmationSender.send(email, token)

        apply(UserResetPasswordRequested(userId = id, email = email, updatedAt = Clock.System.now()))
    }

    suspend fun confirmResetPassword(token: String, newPassword: HashedPassword, otp: OTP) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(otp.verify(id, token)) {
            "Invalid token"
        }

        apply(UserResetPasswordConfirmed(userId = id, newPassword = newPassword, updatedAt = Clock.System.now()))
    }

    fun changePersonalName(personalName: PersonalName) {
        check(deletedAt == null) {
            "User is deleted"
        }

        apply(UserPersonalNameChanged(userId = id, personalName = personalName, updatedAt = Clock.System.now()))
    }

    fun delete() {
        check(deletedAt != null) {
            "User is already deleted"
        }

        apply(UserDeleted(userId = id, deletedAt = Clock.System.now()))
    }

    @EventSourcingHandler
    private fun on(event: UserCreated) {
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

    @EventSourcingHandler
    private fun on(event: UserPasswordChanged) {
        password = event.newPassword
        updatedAt = event.updatedAt
    }

    @EventSourcingHandler
    private fun on(event: UserResetPasswordConfirmed) {
        password = event.newPassword
        updatedAt = event.updatedAt
    }

    @EventSourcingHandler
    private fun on(event: UserActivated) {
        isEmailVerified = true
        updatedAt = event.activatedAt
    }

    @EventSourcingHandler
    private fun on(event: UserDeleted) {
        deletedAt = event.deletedAt
    }

    @EventSourcingHandler
    private fun on(event: UserPersonalNameChanged) {
        personalName = event.personalName
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

            user.apply(UserCreated(id = id, email = email, password = password, createdAt = Clock.System.now()))

            return user
        }
    }
}
