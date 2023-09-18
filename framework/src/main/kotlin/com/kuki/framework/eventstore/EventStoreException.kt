package com.kuki.framework.eventstore

import com.kuki.framework.domain.DomainMessage

sealed class EventStoreException(message: String) : RuntimeException(message) {

    data class EventStreamNotFound(override val message: String) : EventStoreException(message)

    data class DuplicateVersionException(override val message: String) : EventStoreException(message) {
        constructor(event: DomainMessage) : this("Duplicate version from event [$event]")
    }
}
