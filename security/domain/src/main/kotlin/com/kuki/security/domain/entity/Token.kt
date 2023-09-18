package com.kuki.security.domain.entity

import com.kuki.security.domain.exception.TokenException
import com.kuki.security.domain.service.crypto.TokenBackend
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.math.BigInteger
import java.util.*
import kotlin.time.Duration

abstract class Token(
    val tokenBackend: TokenBackend,
    token: String? = null,
    private val tokenType: String,
    private val lifetime: Duration,
) {
    protected var payload: MutableMap<String, Any?> = mutableMapOf()

    init {
        if (token != null) {
            payload = tokenBackend.decode(token).toMutableMap()
            verify()
        } else {
            val currentTime = Clock.System.now()

            payload[TOKEN_TYPE_CLAIM] = tokenType
            setExpiration(currentTime)
            setIat(currentTime)

            setJti()
        }
    }

    fun setClaim(claim: String, value: Any?) {
        payload[claim] = value
    }

    fun setJti() {
        payload[JTI_CLAIM] = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .let {
                BigInteger(it, RADIX).toString(RADIX)
            }
    }

    fun setExpiration(fromTime: Instant? = null) {
        payload[EXP_CLAIM] = (fromTime ?: Clock.System.now()).plus(lifetime).epochSeconds
    }

    fun setIat(atTime: Instant? = null) {
        payload[IAT_CLAIM] = (atTime ?: Clock.System.now()).epochSeconds
    }

    fun setUserId(userId: UserId) {
        payload[USER_ID_CLAIM] = userId.toString()
        payload[SUBJECT_CLAIM] = userId.toString()
    }

    protected open fun verifyTokenType() {
        if (!payload.containsKey(TOKEN_TYPE_CLAIM)) {
            throw TokenException("Token has no type")
        }

        if (this.tokenType != payload[TOKEN_TYPE_CLAIM]) {
            throw TokenException("Token has wrong type")
        }
    }

    private fun verify() {
        val currentTime = Clock.System.now().epochSeconds

        val claimTime = payload[EXP_CLAIM] as Int

        if (claimTime <= currentTime) {
            throw TokenException("Token exp claim has expired")
        }

        if (!payload.containsKey(JTI_CLAIM)) {
            throw TokenException("Token has no id")
        }

        verifyTokenType()
    }

    override fun toString(): String {
        return tokenBackend.encode(payload)
    }

    companion object {
        const val TOKEN_TYPE_CLAIM = "token_type"
        const val JTI_CLAIM = "jti"
        const val USER_ID_CLAIM = "user_id"
        const val SUBJECT_CLAIM = "sub"
        const val EXP_CLAIM = "exp"
        const val IAT_CLAIM = "iat"
        const val RADIX = 16
    }
}
