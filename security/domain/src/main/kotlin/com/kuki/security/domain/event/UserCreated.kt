package com.kuki.security.domain.event

import com.kuki.framework.domain.Event
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserCreated(
    val id: UserId,
    val email: Email,
    val password: HashedPassword,
    val createdAt: Instant,
) : Event
