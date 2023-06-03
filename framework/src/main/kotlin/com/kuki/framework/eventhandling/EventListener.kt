package com.kuki.framework.eventhandling

import com.kuki.framework.domain.Event

interface EventListener {

    suspend fun handle(event: Event)
}
