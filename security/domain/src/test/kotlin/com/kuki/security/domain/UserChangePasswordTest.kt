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


class UserChangePasswordTest {

    private val repository = object : EventSourcingRepository(SimpleEventBus(), InMemoryEventStore(), User::class) {}

    private val userId = UserId.fromString(UUID.randomUUID().toString())

    private val email = Email.fromString("john.doe@email.com")

    @BeforeTest
    fun setupTest() = runTest {
        val aggregate = User.create(
            userId,
            email,
            HashedPassword.encode("password", Mock.passwordEncryption),
            Mock.emailUniqueEmailSpecification(true)
        )

        repository.save(aggregate)
    }

    @Test
    fun `user change password successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        val newPassword = HashedPassword.encode("password2", Mock.passwordEncryption)

        user.changePassword(
            "password",
            newPassword,
            Mock.passwordEncryption
        )

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertEquals(updatedUser.password, newPassword)
    }

    @Test
    fun `user try to change password with invalid current password`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newPassword = HashedPassword.encode("password2", Mock.passwordEncryption)

        assertFailsWith<IllegalArgumentException> {
            user.changePassword(
                "password33",
                newPassword,
                Mock.passwordEncryption
            )
        }
    }

    @Test
    fun `user try to change email with same password`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newPassword = HashedPassword.encode("password", Mock.passwordEncryption)

        assertFailsWith<IllegalArgumentException> {
            user.changePassword(
                "password",
                newPassword,
                Mock.passwordEncryption
            )
        }
    }
}
