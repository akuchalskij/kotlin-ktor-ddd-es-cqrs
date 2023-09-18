package com.kuki.security.ui.http.models.output

import kotlinx.serialization.Serializable

@Serializable
data class ProblemDetail(
    val type: String,
    val title: String,
    val detail: String,
    val status: Int,
    val instance: String
)
