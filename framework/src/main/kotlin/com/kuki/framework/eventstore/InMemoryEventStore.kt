package com.kuki.framework.eventstore

import com.kuki.framework.domain.DomainMessage
import org.slf4j.LoggerFactory

/**
 * In-memory implementation of [EventStore].
 */
class InMemoryEventStore : EventStore {

    private val events: HashMap<String, List<DomainMessage>> = hashMapOf()

    override suspend fun load(id: String): List<DomainMessage> {
        val eventStream = events.filter { event -> event.key == id }

        if (eventStream.isEmpty()) {
            throw EventStoreException.EventStreamNotFound("Not found events for aggregate with id $id")
        }

        logger.info("Events of $id: $eventStream")

        return eventStream
            .values
            .flatten()
    }

    override suspend fun append(id: String, events: List<DomainMessage>) {
        events.forEach { event ->
            if (this.events[id]?.any { message -> message.sequenceNumber == event.sequenceNumber } == true) {
                throw EventStoreException.DuplicateVersionException(event)
            }
        }

        this.events[id] = this.events[id]?.plus(events) ?: events

        logger.info("Events of $id: ${this.events[id]}")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(InMemoryEventStore::class.java)
    }
}
