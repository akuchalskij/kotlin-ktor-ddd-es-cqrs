package com.kuki.framework.eventsourcing

import com.kuki.framework.domain.AggregateRoot
import com.kuki.framework.eventhandling.EventBus
import com.kuki.framework.eventstore.EventStore
import com.kuki.framework.eventstore.EventStoreException
import com.kuki.framework.repository.Repository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class EventSourcingRepository(
    private val eventBus: EventBus,
    private val eventStore: EventStore,
    private val aggregateKlass: KClass<out AggregateRoot>,
) : Repository {

    override suspend fun load(aggregateIdentifier: String): AggregateRoot {
        try {
            val events = eventStore.load(aggregateIdentifier)

            val aggregate = aggregateKlass.createInstance()
            aggregate.initializeState(events)

            return aggregate
        } catch (ex: EventStoreException.EventStreamNotFound) {
            error("Aggregate with id $aggregateIdentifier not found. ${ex.message}")
        }
    }

    override suspend fun save(aggregate: AggregateRoot) {
        val domainEvents = aggregate.domainEvents()

        eventStore.append(aggregate.aggregateId, domainEvents)
        eventBus.publish(domainEvents)
    }
}
