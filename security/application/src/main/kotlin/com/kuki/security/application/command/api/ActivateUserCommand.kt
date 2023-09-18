package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.UserId

data class ActivateUserCommand(val userId: UserId, val token: String) : Command {

    constructor(userId: String, token: String) : this(UserId.fromString(userId), token)
}
