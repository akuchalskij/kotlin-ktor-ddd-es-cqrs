package com.kuki.framework.eventstore

import com.kuki.framework.domain.DomainMessage

interface EventStore {

    suspend fun load(id: String): List<DomainMessage>

    suspend fun append(id: String, events: List<DomainMessage>)
}
