package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId

data class ChangePasswordCommand(
    val userId: UserId,
    val password: HashedPassword
) : Command {

    constructor(uuid: String, password: String, passwordEncryption: PasswordEncryption) : this(
        UserId.fromString(uuid),
        HashedPassword.encode(password, passwordEncryption)
    )
}

