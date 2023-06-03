package com.kuki.framework.domain

import kotlinx.datetime.Instant
import java.io.Serializable

data class DomainMessage(
    val id: String,
    val payload: Event,
    val sequenceNumber: Long,
    val occurredOn: Instant,
) : Serializable
