package com.kuki.security.domain.event

import com.kuki.framework.domain.Event
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserEmailChanged(
    val userId: UserId,
    val email: Email,
    val updatedAt: Instant,
) : Event
