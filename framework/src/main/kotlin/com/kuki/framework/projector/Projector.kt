package com.kuki.framework.projector

import com.kuki.framework.domain.Event
import com.kuki.framework.eventhandling.EventListener
import com.kuki.framework.queryhandling.Query
import com.kuki.framework.queryhandling.QueryHandler
import com.kuki.framework.queryhandling.QueryListener
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.isAccessible

abstract class Projector : EventListener, QueryListener {
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

    override suspend fun ask(query: Query): Any? {
        val method = this::class.functions
            .find { method ->
                method.findAnnotation<QueryHandler>() != null &&
                    method.parameters.size == 2 &&
                    method.parameters[1].type.classifier == query::class
            }
            ?: error("Query handler for [$query] not found")

        return method.callSuspend(this, query)
    }
}
