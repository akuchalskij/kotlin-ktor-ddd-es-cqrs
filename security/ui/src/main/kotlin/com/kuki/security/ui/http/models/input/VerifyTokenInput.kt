package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class VerifyTokenInput(val token: String)
