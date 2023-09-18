package com.kuki.framework.eventsourcing

import com.kuki.framework.domain.AggregateRoot
import com.kuki.framework.eventhandling.EventBus
import com.kuki.framework.eventstore.EventStore
import com.kuki.framework.eventstore.EventStoreException
import com.kuki.framework.repository.Repository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * EventSourcingRepository is a wrapper around the EventStore and EventBus.
 * It is responsible for loading and saving aggregates.
 *
 * @property eventBus  The event bus.
 * @property eventStore The event store.
 * @property aggregateKlass The aggregate class.
 */
open class EventSourcingRepository(
    private val eventBus: EventBus,
    private val eventStore: EventStore,
    private val aggregateKlass: KClass<out AggregateRoot>,
) : Repository {

    /**
     * Loads an aggregate from the event store.
     * @param aggregateIdentifier The aggregate identifier.
     * @return The aggregate.
     */
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

    /**
     * Saves an aggregate to the event store.
     * @param aggregate The aggregate.
     */
    override suspend fun save(aggregate: AggregateRoot) {
        val domainEvents = aggregate.domainEvents()

        eventStore.append(aggregate.aggregateId, domainEvents)
        eventBus.publish(domainEvents)
    }
}
