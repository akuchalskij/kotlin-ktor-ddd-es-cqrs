package com.kuki.framework.commandhandling

interface CommandBus {

    fun subscribe(listener: CommandListener<Command>)

    suspend fun dispatch(command: Command)
}
