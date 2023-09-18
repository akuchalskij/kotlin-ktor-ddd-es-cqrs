package com.kuki.security.domain.repository

import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId

interface CheckUserByEmailInterface {

    suspend fun existsByEmail(email: Email): UserId?
}
