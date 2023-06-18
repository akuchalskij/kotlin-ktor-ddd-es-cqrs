package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId

data class ChangeEmailCommand(
    val userId: UserId,
    val currentPassword: String,
    val email: Email,
) : Command {
    constructor(userId: String, currentPassword: String, email: String) : this(
        UserId.fromString(userId),
        currentPassword,
        Email.fromString(email)
    )
}
