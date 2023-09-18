package com.kuki.framework.projector

sealed class ProjectionException(message: String) : RuntimeException(message) {

    data class ProjectionNotFound(override val message: String) : ProjectionException(message)
}
