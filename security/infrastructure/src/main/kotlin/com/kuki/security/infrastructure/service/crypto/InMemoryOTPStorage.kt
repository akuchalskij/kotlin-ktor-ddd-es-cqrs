package com.kuki.security.infrastructure.service.crypto

import com.kuki.security.domain.valueobject.UserId

class InMemoryOTPStorage : OTPStorage<UserId, String> {

    private val storage = mutableMapOf<UserId, String>()

    override suspend fun get(key: UserId): String? {
        return storage[key]
    }

    override suspend fun set(key: UserId, value: String) {
        storage[key] = value
    }
}
