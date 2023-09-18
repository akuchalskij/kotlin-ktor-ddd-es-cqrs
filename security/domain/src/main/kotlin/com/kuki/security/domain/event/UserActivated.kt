package com.kuki.security.domain.event

import com.kuki.framework.domain.Event
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserActivated(
    val userId: UserId,
    val activatedAt: Instant
) : Event
