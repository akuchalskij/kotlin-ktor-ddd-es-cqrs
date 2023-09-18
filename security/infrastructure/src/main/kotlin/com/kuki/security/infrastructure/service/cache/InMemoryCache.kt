package com.kuki.security.infrastructure.service.cache

import kotlinx.datetime.Instant

@Suppress("UNCHECKED_CAST")
class InMemoryCache : CacheInterface {

    private val store: HashMap<String, Any> = hashMapOf()

    override suspend fun <T : Any> get(key: String): T? {
        return store[key] as? T
    }

    override suspend fun <T : Any> set(key: String, value: T, expiresAt: Instant?) {
        store[key] = value
    }

    override suspend fun remove(key: String) {
        store.remove(key)
    }
}
