package com.kuki.framework.commandhandling

interface CommandHandler<T : Command> {

    suspend fun execute(command: T)
}
