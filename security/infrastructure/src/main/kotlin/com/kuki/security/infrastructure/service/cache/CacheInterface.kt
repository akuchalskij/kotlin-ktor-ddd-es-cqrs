package com.kuki.security.infrastructure.service.cache

import kotlinx.datetime.Instant

interface CacheInterface {

    suspend fun <T : Any> get(key: String): T?

    suspend fun <T : Any> set(key: String, value: T, expiresAt: Instant? = null)

    suspend fun remove(key: String)
}
