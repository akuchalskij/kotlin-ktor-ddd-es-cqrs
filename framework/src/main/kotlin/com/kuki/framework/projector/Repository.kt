package com.kuki.framework.projector

/**
 * Repository interface. All repositories must implement this interface.
 *
 * @param T Type of the entity.
 * @param K Type of the identifier.
 */
interface Repository<T, K> {

    suspend fun save(data: T)

    suspend fun findById(id: K): T

    suspend fun findAll(): List<T>

    suspend fun findAll(limit: Long, offset: Long): List<T>
}
