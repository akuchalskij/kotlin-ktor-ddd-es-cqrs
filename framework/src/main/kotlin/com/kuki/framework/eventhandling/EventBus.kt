package com.kuki.framework.eventhandling

import com.kuki.framework.domain.DomainMessage

interface EventBus {

    fun subscribe(listener: EventListener)

    suspend fun publish(events: List<DomainMessage>)
}
