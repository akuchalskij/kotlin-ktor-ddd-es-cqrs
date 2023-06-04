package com.kuki.security.ui.http.models.output

import com.kuki.security.infrastructure.projector.UserView
import kotlinx.serialization.Serializable

@Serializable
data class UserOutput(
    val id: String,

    val email: String,

    val isEmailVerified: Boolean,

    val firstName: String?,

    val lastName: String?,

    val createdAt: String?,

    val updatedAt: String?
)

fun UserView.serialize() = UserOutput(
    id,
    email,
    isEmailVerified,
    firstName,
    lastName,
    createdAt.toString(),
    updatedAt.toString()
)
