package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.UserId

data class DeleteUserCommand(val userId: UserId) : Command {

    constructor(userId: String) : this(UserId.fromString(userId))
}
