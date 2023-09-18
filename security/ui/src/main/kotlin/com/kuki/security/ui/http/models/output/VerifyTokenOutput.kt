package com.kuki.security.ui.http.models.output

import kotlinx.serialization.Serializable

@Serializable
data class VerifyTokenOutput(val token: String)
