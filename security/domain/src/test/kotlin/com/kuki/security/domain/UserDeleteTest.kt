package com.kuki.security.domain

import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventsourcing.EventSourcingRepository
import com.kuki.framework.eventstore.InMemoryEventStore
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.PersonalName
import com.kuki.security.domain.valueobject.UserId
import kotlinx.coroutines.test.runTest
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull


class UserDeleteTest {

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
    fun `user delete successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertNotNull(updatedUser.deletedAt)
    }


    @Test
    fun `user try to delete again`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertFailsWith<IllegalStateException> {
            updatedUser.delete()
        }
    }

    @Test
    fun `user try to sign in after delete`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertFailsWith<IllegalStateException> {
            updatedUser.signIn(email, "password", Mock.passwordEncryption)
        }
    }

    @Test
    fun `user try to change email after delete`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertFailsWith<IllegalStateException> {
            updatedUser.changeEmail(
                "password",
                Email.fromString("john.doe2@email.com"),
                Mock.emailUniqueEmailSpecification(true),
                Mock.passwordEncryption
            )
        }
    }

    @Test
    fun `user try to change password after delete`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertFailsWith<IllegalStateException> {
            updatedUser.changePassword(
                "password",
                HashedPassword.encode("newPassword", Mock.passwordEncryption),
                Mock.passwordEncryption
            )
        }
    }


    @Test
    fun `user try to change personal name after delete`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.delete()

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertFailsWith<IllegalStateException> {
            updatedUser.changePersonalName(PersonalName.fromString("John", "Ivan", "Doe"))
        }
    }
}
