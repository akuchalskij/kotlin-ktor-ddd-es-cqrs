package com.kuki.framework.projector

import com.kuki.framework.domain.Event
import com.kuki.framework.eventhandling.EventListener
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible

/**
 * Projector is the base class for all projectors.
 * It provides a method to handle events.
 */
abstract class Projector : EventListener {

    /**
     * Handles the given event.
     * It invokes the methods annotated with @EventHandler.
     * @param event the event to handle
     */
    override suspend fun handle(event: Event) {
        val methods = this::class.functions
            .filter { method -> method.findAnnotation<EventHandler>() != null && method.parameters.size == 2 }

        for (method in methods) {
            if (method.parameters[1].type.classifier == event::class) {
                method.isAccessible = true
                method.callSuspend(this, event)
            }
        }
    }
}
