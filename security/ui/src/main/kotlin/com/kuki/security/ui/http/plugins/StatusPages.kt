package com.kuki.security.ui.http.plugins

import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.ui.http.models.output.ProblemDetail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val (message, status) = when (cause) {
                is BadRequestException,
                is IllegalArgumentException -> Pair(
                    ProblemDetail(
                        type = cause::class.simpleName.toString(),
                        title = "Bad Request",
                        detail = cause.message ?: cause.stackTraceToString(),
                        status = HttpStatusCode.BadRequest.value,
                        instance = call.request.uri
                    ),
                    HttpStatusCode.BadRequest
                )

                is IllegalStateException -> Pair(
                    ProblemDetail(
                        type = cause::class.simpleName.toString(),
                        title = "Conflict",
                        detail = cause.message ?: cause.stackTraceToString(),
                        status = HttpStatusCode.Conflict.value,
                        instance = call.request.uri
                    ),
                    HttpStatusCode.Conflict
                )

                is InvalidCredentialsException -> Pair(
                    ProblemDetail(
                        type = cause::class.simpleName.toString(),
                        title = "Invalid Credentials",
                        detail = cause.message,
                        status = HttpStatusCode.Forbidden.value,
                        instance = call.request.uri
                    ),
                    HttpStatusCode.Forbidden
                )

                else -> Pair(
                    ProblemDetail(
                        type = cause::class.simpleName.toString(),
                        title = "Internal Server Error",
                        detail = cause.message ?: cause.stackTraceToString(),
                        status = HttpStatusCode.InternalServerError.value,
                        instance = call.request.uri
                    ),
                    HttpStatusCode.InternalServerError
                )
            }

            call.respond(message = message, status = status)
        }
    }
}
