package com.kuki.security.infrastructure.service.crypto

interface OTPStorage<K, V> {

    suspend fun get(key: K): V?

    suspend fun set(key: K, value: V)
}
