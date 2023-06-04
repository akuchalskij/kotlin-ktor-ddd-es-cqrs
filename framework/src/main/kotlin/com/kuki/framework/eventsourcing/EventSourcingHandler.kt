package com.kuki.framework.eventsourcing

/**
 * EventSourcingHandler
 *
 * Marks a method as an event sourcing handler.
 * It is used to mark a method that should be called when an event is received.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class EventSourcingHandler
