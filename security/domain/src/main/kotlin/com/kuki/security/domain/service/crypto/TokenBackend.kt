package com.kuki.security.domain.service.crypto

interface TokenBackend {

    fun encode(payload: Map<String, Any?>): String

    fun decode(rawToken: String): Map<String, Any?>
}
