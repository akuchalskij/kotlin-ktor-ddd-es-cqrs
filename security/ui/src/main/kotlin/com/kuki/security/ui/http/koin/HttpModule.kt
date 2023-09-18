package com.kuki.security.ui.http.koin

import com.kuki.security.domain.service.crypto.TokenBackend
import com.kuki.security.infrastructure.service.crypto.JWTTokenBackend
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

fun httpModule(application: Application) = module {
    single<CoroutineScope> {
        CoroutineScope(application.coroutineContext)
    }

    single<TokenBackend> {
        JWTTokenBackend(
            audience = application.environment.config.property("jwt.audience").getString(),
            issuer = application.environment.config.property("jwt.issuer").getString(),
            secret = application.environment.config.property("jwt.secret").getString(),
            payloadClaimParser = get(),
        )
    }
}
