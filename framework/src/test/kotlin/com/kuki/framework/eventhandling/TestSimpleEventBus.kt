package com.kuki.framework.eventhandling

import com.kuki.framework.domain.DomainMessage
import java.util.*

class TestSimpleEventBus : EventBus {

    val eventListeners: MutableList<EventListener> = mutableListOf()
    val eventQueue: Queue<DomainMessage> = LinkedList()
    var isPublishing: Boolean = false

    override fun subscribe(listener: EventListener) {
        eventListeners.add(listener)
    }

    override suspend fun publish(events: List<DomainMessage>) {
        eventQueue.addAll(events)

        if (!isPublishing) {
            isPublishing = true

            try {
                while (eventQueue.isNotEmpty()) {
                    val event = eventQueue.remove()

                    eventListeners.forEach { listener ->
                        listener.handle(event.payload)
                    }
                }
            } finally {
                isPublishing = false
            }
        }
    }
}
