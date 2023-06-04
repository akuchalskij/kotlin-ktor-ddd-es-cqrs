package com.kuki.framework.eventhandling

import com.kuki.framework.domain.DomainMessage
import java.util.*

/**
 * Simple EventBus implementation.
 *
 * It provides a simple way to publish events to listeners.
 *
 * @property eventListeners  List of event listeners.
 * @property eventQueue      Event queue.
 * @property isPublishing    Flag to indicate if the event bus is publishing.
 */
class SimpleEventBus : EventBus {

    private val eventListeners: MutableList<EventListener> = mutableListOf()
    private val eventQueue: Queue<DomainMessage> = LinkedList()
    private var isPublishing: Boolean = false

    /**
     * Subscribe a listener to the event bus.
     * @param listener  Event listener.
     * @see EventListener
     */
    override fun subscribe(listener: EventListener) {
        eventListeners.add(listener)
    }

    /**
     * Publish events to listeners.
     * @param events  List of events.
     * @see DomainMessage
     */
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
