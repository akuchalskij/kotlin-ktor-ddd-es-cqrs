package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordConfirmInput(val token: String, val newPassword: String)
