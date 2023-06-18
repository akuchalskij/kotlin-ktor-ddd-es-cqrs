package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId

data class RequestResetPasswordCommand(val userId: UserId, val email: Email) : Command {

    constructor(userId: String, email: String) : this(UserId.fromString(userId), Email.fromString(email))
}
