package com.kuki.security.ui.http.routes

import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.security.application.command.api.SignInCommand
import com.kuki.security.application.query.api.GetNewAccessTokenQuery
import com.kuki.security.application.query.api.GetTokenQuery
import com.kuki.security.application.query.api.GetVerifiedTokenQuery
import com.kuki.security.domain.valueobject.Tokens
import com.kuki.security.ui.http.models.input.RefreshTokenInput
import com.kuki.security.ui.http.models.input.SignInInput
import com.kuki.security.ui.http.models.input.VerifyTokenInput
import com.kuki.security.ui.http.models.output.TokenPairOutput
import com.kuki.security.ui.http.models.output.VerifyTokenOutput
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

        val token = queryBus.ask<Tokens>(GetTokenQuery(request.email))

        call.respond(HttpStatusCode.OK, TokenPairOutput(token.accessToken, token.refreshToken))
    }

    post<JWTResource.Verify> {
        val request = call.receive<VerifyTokenInput>()

        val token = queryBus.ask<String>(GetVerifiedTokenQuery(request.token))

        call.respond(HttpStatusCode.OK, VerifyTokenOutput(token))
    }

    authenticate {
        post<JWTResource.Refresh> {
            val request = call.receive<RefreshTokenInput>()

            val token = queryBus.ask<Tokens>(GetNewAccessTokenQuery(request.refreshToken))

            call.respond(HttpStatusCode.OK, TokenPairOutput(token.accessToken, token.refreshToken))
        }
    }
}
