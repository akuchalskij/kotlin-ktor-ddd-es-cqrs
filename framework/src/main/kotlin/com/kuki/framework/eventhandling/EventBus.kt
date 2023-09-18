package com.kuki.framework.eventhandling

import com.kuki.framework.domain.DomainMessage

interface EventBus {

    /**
     * Subscribe event listener
     */
    fun subscribe(listener: EventListener)

    /**
     * Publish event to listeners
     *
     * @param events List of DomainMessage
     */
    suspend fun publish(events: List<DomainMessage>)
}
