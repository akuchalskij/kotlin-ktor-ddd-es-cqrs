package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId

data class CreateUserCommand(
    val userId: UserId,
    val email: Email,
    val password: HashedPassword
) : Command {

    constructor(uuid: String, email: String, password: String, passwordEncryption: PasswordEncryption) : this(
        UserId.fromString(uuid),
        Email.fromString(email),
        HashedPassword.encode(password, passwordEncryption)
    )
}
