package com.kuki.security.domain.exception

data class InvalidCredentialsException(override val message: String) : Exception(message)
