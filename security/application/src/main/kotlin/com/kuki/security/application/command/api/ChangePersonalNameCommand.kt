package com.kuki.security.application.command.api

import com.kuki.framework.commandhandling.Command
import com.kuki.security.domain.valueobject.PersonalName
import com.kuki.security.domain.valueobject.UserId

data class ChangePersonalNameCommand(
    val userId: UserId,
    val personalName: PersonalName,
) : Command {

    constructor(
        userId: String,
        givenName: String,
        middleName: String,
        surname: String,
    ) : this(UserId.fromString(userId), PersonalName.fromString(givenName, middleName, surname))
}
