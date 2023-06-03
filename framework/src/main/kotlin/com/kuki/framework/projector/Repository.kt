package com.kuki.framework.projector

interface Repository<T, K> {

    suspend fun save(data: T)

    suspend fun findById(id: K): T

    suspend fun findAll(): List<T>

    suspend fun findAll(limit: Long, offset: Long): List<T>
}
