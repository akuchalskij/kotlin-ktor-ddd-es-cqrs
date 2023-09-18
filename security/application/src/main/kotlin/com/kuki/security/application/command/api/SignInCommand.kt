package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.Email

data class SignInCommand(
    val email: Email,
    val password: String
) : Command {

    constructor(email: String, password: String) : this(Email.fromString(email), password)
}
