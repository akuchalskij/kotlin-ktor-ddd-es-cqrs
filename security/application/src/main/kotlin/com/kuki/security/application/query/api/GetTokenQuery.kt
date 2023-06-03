package com.kuki.security.application.query.api

import com.kuki.framework.queryhandling.Query
import com.kuki.security.domain.valueobject.Email

data class GetTokenQuery(val email: Email) : Query {
    constructor(email: String) : this(Email.fromString(email))
}
