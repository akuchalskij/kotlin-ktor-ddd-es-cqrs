package com.kuki.security.ui.http.plugins

import com.kuki.security.ui.http.routes.jwtRoutes
import com.kuki.security.ui.http.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources)

    routing {
        userRoutes()
        jwtRoutes()
    }
}
