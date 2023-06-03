package com.kuki.framework.repository

import com.kuki.framework.domain.AggregateRoot

interface Repository {
    suspend fun load(aggregateIdentifier: String): AggregateRoot

    suspend fun save(aggregate: AggregateRoot)
}
