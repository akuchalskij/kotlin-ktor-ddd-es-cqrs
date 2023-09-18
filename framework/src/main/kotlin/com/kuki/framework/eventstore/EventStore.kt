package com.kuki.framework.eventstore

import com.kuki.framework.domain.DomainMessage

/**
 * Event Store Interface for persisting domain events.
 */
interface EventStore {

    /**
     * Load all events for a given aggregate id.
     * @param id the aggregate id
     * @return the list of domain events
     */
    suspend fun load(id: String): List<DomainMessage>

    /**
     * Append a list of domain events to the store.
     * @param id the aggregate id
     * @param events the list of domain events
     */
    suspend fun append(id: String, events: List<DomainMessage>)
}
