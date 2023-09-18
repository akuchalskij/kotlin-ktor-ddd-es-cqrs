package com.kuki.framework.domain

import com.kuki.framework.eventsourcing.EventSourcingHandler
import kotlinx.datetime.Clock
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * AggregateRoot is the base class for all aggregate root.
 *
 */
abstract class AggregateRoot {

    private var aggregateVersion: Long = -1
    private val domainEvents: MutableList<DomainMessage> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    val aggregateId: String
        get() = this::class.memberProperties
            .firstOrNull { it.findAnnotation<AggregateId>() != null }
            ?.let { property ->
                property.isAccessible = true
                (property as KProperty1<AggregateRoot, *>).get(this).toString()
            }
            ?: error("No property found with @AggregateId annotation")

    /**
     * Method to get all domain events that have been raised by the aggregate root.
     *
     * @return all domain events that have been raised by the aggregate root.
     */
    fun domainEvents(): List<DomainMessage> {
        val events = domainEvents.toMutableList()
        domainEvents.clear()
        return events
    }

    /**
     * Method to initialize the state of the aggregate root.
     *
     * @param events the list of domain events
     */
    fun initializeState(events: List<DomainMessage>) {
        val methods = this::class.functions
            .filter { method -> method.findAnnotation<EventSourcingHandler>() != null && method.parameters.size == 2 }

        for (event in events) {
            aggregateVersion = event.sequenceNumber

            for (method in methods) {
                if (method.parameters[1].type.classifier == event.payload::class) {
                    method.isAccessible = true
                    method.call(this, event.payload)
                }
            }
        }
    }

    /**
     * Apply the event to the aggregate root.
     *
     * @param event the event to apply
     */
    protected fun apply(event: Event) {
        ++aggregateVersion
        domainEvents.add(DomainMessage(UUID.randomUUID().toString(), event, aggregateVersion, Clock.System.now()))
    }
}
