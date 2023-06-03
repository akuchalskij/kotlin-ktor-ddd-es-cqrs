package com.kuki.framework.queryhandling

import kotlin.reflect.full.functions

class SimpleQueryBus : QueryBus {

    private val queryListeners: MutableList<QueryListener> = mutableListOf()

    override fun subscribe(listener: QueryListener) {
        queryListeners.add(listener)
    }

    override suspend fun <R> ask(query: Query): R {
        @Suppress("UNCHECKED_CAST")
        return (queryListeners
            .find { listener ->
                listener::class.functions.find { method ->
                    method.parameters[1].type.classifier == query::class
                } != null
            }
            ?.ask(query) as R
            ?: error("Not found query listener which handle [$query]"))
    }
}

