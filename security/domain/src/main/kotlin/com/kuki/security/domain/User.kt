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

class User() : AggregateRoot() {

    @AggregateId
    lateinit var id: UserId

    lateinit var email: Email
        private set

    lateinit var password: HashedPassword
        private set

    var isEmailVerified: Boolean = false
        private set

    var personalName: PersonalName? = null
        private set

    var createdAt: Instant? = null
        private set

    var updatedAt: Instant? = null
        private set

    var deletedAt: Instant? = null
        private set

    fun signIn(email: Email, plainPassword: String, passwordEncryption: PasswordEncryption) {
        check(deletedAt == null) {
            "User is deleted"
        }

        require(this.email == email) {
            "Email should be same as user's email"
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

        check(uniqueEmailSpecification.isUnique(email.toString())) {
            "Email $email is already taken"
        }

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
        check(deletedAt == null) {
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
            val user = User().apply { this.id = id }

            check(uniqueEmailSpecification.isUnique(email.toString())) {
                "Email $email is already taken"
            }

            user.apply(UserCreated(id = id, email = email, password = password, createdAt = Clock.System.now()))

            return user
        }
    }
}
