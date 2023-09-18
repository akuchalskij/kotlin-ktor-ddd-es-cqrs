package com.kuki.framework.commandhandling

import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class SimpleCommandBusTest {

    private lateinit var commandBus: CommandBus

    @BeforeTest
    fun setUp() {
        commandBus = SimpleCommandBus()
    }

    @Test
    fun `should subscribe and execute received commands`() {
        val commandHandler = object : CommandHandler<Command> {
            override suspend fun execute(command: Command) {
                assertIs<ExampleCommand>(command)
            }
        }

        commandBus.subscribe(commandHandler)

        runTest {
            commandBus.dispatch(ExampleCommand("test"))
        }
    }


    data class ExampleCommand(val id: String) : Command
}
