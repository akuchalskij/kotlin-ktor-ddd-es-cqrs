package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.domain.valueobject.HashedPassword
import com.kuki.security.domain.valueobject.UserId

data class ConfirmResetPasswordCommand(
    val userId: UserId,
    val token: String,
    val newPassword: HashedPassword
) : Command {

    constructor(userId: String, token: String, newPassword: String, passwordEncryption: PasswordEncryption) : this(
        UserId.fromString(userId),
        token,
        HashedPassword.encode(newPassword, passwordEncryption)
    )
}
