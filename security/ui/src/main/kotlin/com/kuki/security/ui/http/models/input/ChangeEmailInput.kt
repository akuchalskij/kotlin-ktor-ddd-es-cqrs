package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class ChangeEmailInput(val currentPassword: String, val email: String)
