package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class SignInInput(val email: String, val password: String)
