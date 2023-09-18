package com.kuki.security.domain.exception

data class TokenException(override val message: String) : Exception(message)
