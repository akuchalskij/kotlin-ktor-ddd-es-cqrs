package com.kuki.framework.commandhandling

import java.util.*
import kotlin.reflect.full.starProjectedType

class SimpleCommandBus : CommandBus {

    private val commandListeners: MutableList<CommandListener<Command>> = mutableListOf()
    private val commandQueue: Queue<Command> = LinkedList()
    private var isDispatching: Boolean = false

    override fun subscribe(listener: CommandListener<Command>) {
        commandListeners.add(listener)
    }

    override suspend fun dispatch(command: Command) {
        commandQueue.add(command)

        if (!isDispatching) {
            isDispatching = true

            try {
                while (commandQueue.any { it == command }) {
                    commandQueue.remove(command)
                    commandListeners
                        .filter { listener ->
                            listener::class.supertypes.first().arguments.first().type == command::class.starProjectedType
                        }
                        .forEach { listener ->
                            listener.execute(command)
                        }
                }
            } finally {
                isDispatching = false
            }
        }
    }
}

