package com.kuki.security.ui.http.models.input

import kotlinx.serialization.Serializable

@Serializable
data class ChangePersonalNameInput(
    val givenName: String,
    val middleName: String,
    val surname: String,
)
