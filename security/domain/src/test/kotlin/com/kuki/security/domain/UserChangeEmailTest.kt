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


class UserChangeEmailTest {

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
    fun `user change email successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        val newEmail = Email.fromString("john.doe2@email.com")

        user.changeEmail(
            "password",
            newEmail,
            Mock.emailUniqueEmailSpecification(true),
            Mock.passwordEncryption
        )

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertNotEquals(updatedUser.email, email)
    }

    @Test
    fun `user try to change email with invalid password`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newEmail = Email.fromString("john.doe2@email.com")

        assertFailsWith<IllegalArgumentException> {
            user.changeEmail(
                "password222",
                newEmail,
                Mock.emailUniqueEmailSpecification(true),
                Mock.passwordEncryption
            )
        }
    }

    @Test
    fun `user try to change email with same email`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newEmail = Email.fromString("john.doe@email.com")

        assertFailsWith<IllegalArgumentException> {
            user.changeEmail(
                "password",
                newEmail,
                Mock.emailUniqueEmailSpecification(true),
                Mock.passwordEncryption
            )
        }
    }

    @Test
    fun `user try to change email with email not unique`() = runTest {
        val user = repository.load(userId.toString()) as User
        val newEmail = Email.fromString("john.doe2@email.com")

        assertFailsWith<IllegalStateException> {
            user.changeEmail(
                "password",
                newEmail,
                Mock.emailUniqueEmailSpecification(false),
                Mock.passwordEncryption
            )
        }
    }
}
