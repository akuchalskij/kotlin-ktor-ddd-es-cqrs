package com.kuki.framework.queryhandling

interface QueryListener {

    suspend fun ask(query: Query): Any?
}
