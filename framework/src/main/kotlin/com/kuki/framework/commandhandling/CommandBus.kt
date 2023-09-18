package com.kuki.framework.commandhandling

interface CommandBus {

    /**
     * Subscribe a command handler to the bus
     */
    fun subscribe(listener: CommandHandler<Command>)

    /**
     * Dispatch a command to the bus
     */
    suspend fun dispatch(command: Command)
}
