package com.kuki.security.ui.http.koin

import com.kuki.security.domain.service.crypto.TokenGenerator
import com.kuki.security.ui.http.services.JWTTokenGenerator
import io.ktor.server.application.*
import org.koin.dsl.module

fun httpModule(application: Application) = module {
    single<TokenGenerator> {
        JWTTokenGenerator(
            audience = application.environment.config.property("jwt.audience").getString(),
            issuer = application.environment.config.property("jwt.issuer").getString(),
            secret = application.environment.config.property("jwt.secret").getString()
        )
    }
}
