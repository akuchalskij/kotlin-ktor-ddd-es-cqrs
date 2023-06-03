package com.kuki.security.ui.http.routes

import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.security.application.command.api.ChangeEmailCommand
import com.kuki.security.application.command.api.CreateUserCommand
import com.kuki.security.application.query.api.FindAllUsersQuery
import com.kuki.security.application.query.api.FindUserByIdQuery
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.ui.http.models.input.ChangeEmailInput
import com.kuki.security.ui.http.models.input.SignUpInput
import com.kuki.security.ui.http.models.output.serialize
import com.kuki.security.ui.http.resources.UsersResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val queryBus by inject<QueryBus>()
    val commandBus by inject<CommandBus>()
    val passwordEncryption by inject<PasswordEncryption>()

    post<UsersResource> {
        val request = call.receive<SignUpInput>()

        commandBus.dispatch(CreateUserCommand(request.uuid, request.email, request.password, passwordEncryption))

        call.respond(HttpStatusCode.Created, request)
    }

    authenticate {
        get<UsersResource> { resource ->
            val users = queryBus
                .ask<List<UserView>>(FindAllUsersQuery(resource.limit, resource.offset))
                .map(UserView::serialize)

            call.respond(HttpStatusCode.OK, users)
        }

        get<UsersResource.User> { resource ->
            val user = queryBus.ask<UserView>(FindUserByIdQuery(resource.id))

            call.respond(HttpStatusCode.OK, user.serialize())
        }

        put<UsersResource.User.ChangeEmail> { resource ->
            val request = call.receive<ChangeEmailInput>()

            commandBus.dispatch(ChangeEmailCommand(resource.parent.id, request.email))

            call.respond(HttpStatusCode.OK, request)
        }
    }
}
