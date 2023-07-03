package com.kuki.security.domain

import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventsourcing.EventSourcingRepository
import com.kuki.framework.eventstore.InMemoryEventStore
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId
import kotlinx.coroutines.test.runTest
import java.util.*
import kotlin.test.*


class UserResetPasswordTest {

    private val repository = object : EventSourcingRepository(SimpleEventBus(), InMemoryEventStore(), User::class) {}

    private val userId = UserId.fromString(UUID.randomUUID().toString())

    private val email = Email.fromString("john.doe@email.com")

    @BeforeTest
    fun setupTest() = runTest {
        val aggregate = User.create(
            id = userId,
            email = email,
            password = HashedPassword.encode("password", Mock.passwordEncryption),
            uniqueEmailSpecification = Mock.emailUniqueEmailSpecification(true)
        )

        repository.save(aggregate)
    }

    @Test
    fun `user send reset password request successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.requestResetPassword(email, Mock.resetPasswordConfirmationSender, Mock.otp())
        repository.save(user)
    }

    @Test
    fun `user try to send reset password request with invalid email`() = runTest {
        val user = repository.load(userId.toString()) as User

        assertFailsWith<IllegalArgumentException> {
            user.requestResetPassword(
                email = Email.fromString("john.doe2@email.com"),
                resetPasswordConfirmationSender = Mock.resetPasswordConfirmationSender,
                otp = Mock.otp()
            )
        }
    }

    @Test
    fun `user reset password successful`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newPassword = HashedPassword.encode("newPassword", Mock.passwordEncryption)

        user.confirmResetPassword(
            token = "token",
            newPassword = newPassword,
            otp = Mock.otp()
        )

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertEquals(updatedUser.password, newPassword)
    }

    @Test
    fun `user try to reset password with invalid otp`() = runTest {
        val user = repository.load(userId.toString()) as User

        assertFailsWith<IllegalArgumentException> {
            user.confirmResetPassword(
                token = "token",
                newPassword = HashedPassword.encode("newPassword", Mock.passwordEncryption),
                otp = Mock.otp { _, _ -> false }
            )
        }
    }
}
