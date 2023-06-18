package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordInput(val currentPassword: String, val newPassword: String)
