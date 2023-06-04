package com.kuki.framework.queryhandling

import kotlin.reflect.full.functions

/**
 * Simple implementation of QueryBus.
 */
class SimpleQueryBus : QueryBus {

    private val queryHandlers: MutableList<QueryHandler<Query, *>> = mutableListOf()


    override fun subscribe(listener: QueryHandler<Query, *>) {
        queryHandlers.add(listener)
    }

    override suspend fun <R> ask(query: Query): R {
        @Suppress("UNCHECKED_CAST")
        return (queryHandlers
            .find { listener ->
                listener::class.functions.find { method ->
                    method.parameters.size == 2 && method.parameters[1].type.classifier == query::class
                } != null
            }
            ?.ask(query) as R
            ?: error("Not found query listener which handle [$query]"))
    }
}

