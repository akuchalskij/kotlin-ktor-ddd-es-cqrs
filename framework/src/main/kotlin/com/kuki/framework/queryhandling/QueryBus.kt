package com.kuki.framework.queryhandling

/**
 * Query Bus Interface for Query Handling
 */
interface QueryBus {

    /**
     * Subscribe a listener to the query bus
     */
    fun subscribe(listener: QueryHandler<Query, *>)

    /**
     * Method to send a query to the query bus
     */
    suspend fun <R> ask(query: Query): R
}
