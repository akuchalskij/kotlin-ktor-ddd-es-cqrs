package com.kuki.security.domain

import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventsourcing.EventSourcingRepository
import com.kuki.framework.eventstore.InMemoryEventStore
import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId
import kotlinx.coroutines.test.runTest
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith


class UserSignInEmailTest {

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
    fun `user sign in successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        user.signIn(email, "password", Mock.passwordEncryption)

        repository.save(user)
    }

    @Test
    fun `user try to sign in with invalid password`() = runTest {
        val user = repository.load(userId.toString()) as User

        assertFailsWith<InvalidCredentialsException> {
            user.signIn(email, "password222", Mock.passwordEncryption)
        }
    }

    @Test
    fun `user try to sign in with invalid email`() = runTest {
        val user = repository.load(userId.toString()) as User

        assertFailsWith<IllegalArgumentException> {
            user.signIn(Email.fromString("john.doe2@email.com"), "password", Mock.passwordEncryption)
        }
    }
}
