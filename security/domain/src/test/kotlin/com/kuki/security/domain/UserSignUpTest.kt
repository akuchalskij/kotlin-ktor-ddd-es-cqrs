package com.kuki.security.domain

import com.kuki.framework.eventhandling.SimpleEventBus
import com.kuki.framework.eventsourcing.EventSourcingRepository
import com.kuki.framework.eventstore.InMemoryEventStore
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId
import kotlinx.coroutines.test.runTest
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class UserSignUpTest {

    private val repository = object : EventSourcingRepository(SimpleEventBus(), InMemoryEventStore(), User::class) {}

    @Test
    fun `user sign up successful`() = runTest {
        val userId = UserId.fromString(UUID.randomUUID().toString())
        val email = Email.fromString("john.doe@email.com")

        val aggregate = User.create(
            userId,
            email,
            HashedPassword.encode("password", Mock.passwordEncryption),
            Mock.emailUniqueEmailSpecification(true)
        )

        repository.save(aggregate)

        val user = repository.load(userId.toString()) as User

        assertEquals(user.email, email)
    }

    @Test
    fun `user try to sign up with an existing email`() = runTest {
        assertFailsWith<IllegalStateException> {
            User.create(
                UserId.fromString(UUID.randomUUID().toString()),
                Email.fromString("john.doe@email.com"),
                HashedPassword.encode("password", Mock.passwordEncryption),
                Mock.emailUniqueEmailSpecification(false)
            )
        }
    }
}
