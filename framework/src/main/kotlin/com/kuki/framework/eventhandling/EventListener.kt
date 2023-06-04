package com.kuki.framework.eventhandling

import com.kuki.framework.domain.Event

interface EventListener {

    /**
     * Method for handling events
     * @param event Event to handle
     */
    suspend fun handle(event: Event)
}
