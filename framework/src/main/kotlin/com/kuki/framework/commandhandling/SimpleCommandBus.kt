package com.kuki.framework.commandhandling

import com.kuki.framework.reflect.genericType
import java.util.*
import kotlin.reflect.full.starProjectedType

/**
 * This class is responsible for dispatching commands to the appropriate command handlers.
 * It uses a simple strategy for dispatching commands:
 * - If a command is being dispatched, it is not dispatched again.
 * - If a command is not being dispatched, it is added to a queue.
 * - If a command is added to the queue, it is dispatched to the appropriate command handler.
 */
class SimpleCommandBus : CommandBus {

    private val commandHandlers: MutableList<CommandHandler<Command>> = mutableListOf()
    private val commandQueue: Queue<Command> = LinkedList()
    private var isDispatching: Boolean = false


    /**
     * This method is responsible for subscribing a command handler to the command bus.
     *
     * @param listener The command handler to be subscribed.
     * @see CommandHandler
     */
    override fun subscribe(listener: CommandHandler<Command>) {
        commandHandlers.add(listener)
    }

    /**
     * This method is responsible for dispatching a command to the appropriate command handler.
     *
     * @param command The command to be dispatched.
     * @see Command
     */
    override suspend fun dispatch(command: Command) {
        commandQueue.add(command)

        if (!isDispatching) {
            isDispatching = true

            try {
                while (commandQueue.any { it == command }) {
                    commandQueue.remove(command)
                    commandHandlers
                        .filter { listener ->
                            listener::class.genericType() == command::class.starProjectedType
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

