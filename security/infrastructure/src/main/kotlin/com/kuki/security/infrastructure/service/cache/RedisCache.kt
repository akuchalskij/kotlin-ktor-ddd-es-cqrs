package com.kuki.security.infrastructure.service.cache

import io.github.crackthecodeabhi.kreds.connection.KredsClient
import kotlinx.datetime.Instant

@Suppress("UNCHECKED_CAST")
class RedisCache(private val client: KredsClient) : CacheInterface {

    override suspend fun <T : Any> get(key: String): T? = client.use { session ->
        session.get(key) as? T
    }

    override suspend fun <T : Any> set(key: String, value: T, expiresAt: Instant?) {
        client.use { session ->
            session.set(key, value.toString())
            expiresAt?.let {
                session.expireAt(key, expiresAt.epochSeconds.toULong())
            }
        }
    }

    override suspend fun remove(key: String) {
        client.use { session ->
            session.del(key)
        }
    }
}
