package com.kuki.security.ui.http.routes

import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.security.application.command.api.SignInCommand
import com.kuki.security.application.query.api.GetTokenQuery
import com.kuki.security.ui.http.models.input.SignInInput
import com.kuki.security.ui.http.resources.JWTResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.jwtRoutes() {
    val queryBus by inject<QueryBus>()
    val commandBus by inject<CommandBus>()

    post<JWTResource.Create> {
        val request = call.receive<SignInInput>()

        commandBus.dispatch(SignInCommand(request.email, request.password))

        val token = queryBus.ask<String>(GetTokenQuery(request.email))

        call.respond(HttpStatusCode.OK, hashMapOf("token" to token))
    }

    authenticate {
        post<JWTResource.Refresh> {
            call.respond(HttpStatusCode.OK)
        }

        post<JWTResource.Verify> {
            call.respond(HttpStatusCode.OK)
        }
    }
}
