package com.kuki.security.domain.service.crypto

import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId

interface TokenGenerator {

    suspend fun generate(userId: UserId, email: Email): String
}
