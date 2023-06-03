package com.kuki.framework.commandhandling

interface CommandListener<T : Command> {

    suspend fun execute(command: T)
}
