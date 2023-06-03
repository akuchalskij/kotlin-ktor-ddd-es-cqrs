package com.kuki.framework.commandhandling

import kotlin.test.BeforeTest
import kotlin.test.Test

class SimpleCommandBusTest {

    private lateinit var commandBus: CommandBus

    @BeforeTest
    fun setUp() {
        commandBus = SimpleCommandBus()
    }

    @Test
    fun subscribe() {
    }

    @Test
    fun dispatch() {
    }
}
