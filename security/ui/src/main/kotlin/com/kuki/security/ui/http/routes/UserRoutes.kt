package com.kuki.security.ui.http.routes

import com.kuki.framework.commandhandling.CommandBus
import com.kuki.framework.queryhandling.QueryBus
import com.kuki.security.application.command.api.*
import com.kuki.security.application.query.api.FindUserByIdQuery
import com.kuki.security.domain.exception.InvalidCredentialsException
import com.kuki.security.domain.service.crypto.PasswordEncryption
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.ui.http.models.input.*
import com.kuki.security.ui.http.models.output.serialize
import com.kuki.security.ui.http.resources.UsersResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
        get<UsersResource.User> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val user = queryBus.ask<UserView>(FindUserByIdQuery(userId))

            call.respond(HttpStatusCode.OK, user.serialize())
        }

        put<UsersResource.User> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ChangePersonalNameInput>()

            commandBus.dispatch(
                ChangePersonalNameCommand(
                    userId,
                    request.givenName,
                    request.middleName,
                    request.surname
                )
            )

            call.respond(HttpStatusCode.OK, request)
        }

        delete<UsersResource.User> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            commandBus.dispatch(DeleteUserCommand(userId))

            call.respond(HttpStatusCode.NoContent)
        }

        post<UsersResource.User.SendActivate> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<SendActivateInput>()

            commandBus.dispatch(SendActivationRequestCommand(userId, request.email))

            call.respond(HttpStatusCode.OK, request)
        }

        post<UsersResource.User.Activate> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ActivateInput>()

            commandBus.dispatch(ActivateUserCommand(userId, request.token))

            call.respond(HttpStatusCode.OK, request)
        }

        post<UsersResource.User.ResetPassword> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ResetPasswordInput>()

            commandBus.dispatch(RequestResetPasswordCommand(userId, request.email))

            call.respond(HttpStatusCode.OK, request)
        }

        post<UsersResource.User.ResetPasswordConfirm> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ResetPasswordConfirmInput>()

            commandBus.dispatch(
                ConfirmResetPasswordCommand(
                    userId,
                    request.token,
                    request.newPassword,
                    passwordEncryption
                )
            )

            call.respond(HttpStatusCode.OK, request)
        }

        put<UsersResource.User.ChangeEmail> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ChangeEmailInput>()

            commandBus.dispatch(ChangeEmailCommand(userId, request.currentPassword, request.email))

            call.respond(HttpStatusCode.OK, request)
        }

        put<UsersResource.User.ChangePassword> {
            val userId = call.principal<JWTPrincipal>()
                ?.subject
                ?: throw InvalidCredentialsException("Subject not found")

            val request = call.receive<ChangePasswordInput>()

            commandBus.dispatch(
                ChangePasswordCommand(
                    userId,
                    request.currentPassword,
                    request.newPassword,
                    passwordEncryption
                )
            )

            call.respond(HttpStatusCode.OK, request)
        }
    }
}
