package com.kuki.security.infrastructure.repository

import com.kuki.framework.eventhandling.EventBus
import com.kuki.framework.eventsourcing.EventSourcingRepository
import com.kuki.framework.eventstore.EventStore
import com.kuki.security.domain.User
import com.kuki.security.domain.repository.UserRepositoryInterface
import com.kuki.security.domain.valueobject.UserId

class UserEventStore(eventBus: EventBus, eventStore: EventStore) :
    EventSourcingRepository(eventBus, eventStore, User::class),
    UserRepositoryInterface {
    override suspend fun findById(id: UserId) = load(id.toString()) as User

    override suspend fun store(user: User) {
        save(user)
    }
}
