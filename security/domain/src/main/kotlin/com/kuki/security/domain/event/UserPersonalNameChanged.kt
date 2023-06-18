package com.kuki.security.domain.event

import com.kuki.framework.domain.Event
import com.kuki.security.domain.valueobject.PersonalName
import com.kuki.security.domain.valueobject.UserId
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserPersonalNameChanged(
    val userId: UserId,
    val personalName: PersonalName,
    val updatedAt: Instant,
) : Event
