package com.kuki.security.ui.http.plugins

import com.kuki.security.application.koin.applicationModule
import com.kuki.security.infrastructure.koin.infrastructureModule
import com.kuki.security.ui.http.koin.httpModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            httpModule(this@configureKoin),
            applicationModule,
            infrastructureModule
        )
    }
}
