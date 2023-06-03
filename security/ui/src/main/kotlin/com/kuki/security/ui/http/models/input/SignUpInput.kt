package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class SignUpInput(val uuid: String, val email: String, val password: String)
