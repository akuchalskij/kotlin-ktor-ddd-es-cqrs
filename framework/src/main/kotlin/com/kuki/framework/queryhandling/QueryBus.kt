package com.kuki.framework.queryhandling

interface QueryBus {

    fun subscribe(listener: QueryListener)

    suspend fun <R> ask(query: Query): R
}
