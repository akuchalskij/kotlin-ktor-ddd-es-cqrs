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
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class UserChangePersonalNameTest {

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
    fun `user change personal name successful`() = runTest {
        val user = repository.load(userId.toString()) as User

        val personalName = PersonalName.fromString("John", "Ivan", "Doe")
        user.changePersonalName(personalName)

        repository.save(user)

        val updatedUser = repository.load(userId.toString()) as User

        assertNotEquals(updatedUser.personalName, user.personalName)
        assertEquals(updatedUser.personalName, personalName)
    }
}
