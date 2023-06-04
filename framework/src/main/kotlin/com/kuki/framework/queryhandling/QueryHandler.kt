package com.kuki.framework.queryhandling

/**
 * QueryHandler is an interface that defines a method to handle a query.
 */
interface QueryHandler<Q : Query, R> {

    suspend fun ask(query: Q): R?
}
