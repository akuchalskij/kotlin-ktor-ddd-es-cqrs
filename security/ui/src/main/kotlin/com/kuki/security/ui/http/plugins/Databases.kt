package com.kuki.security.ui.http.plugins

import com.kuki.security.infrastructure.projector.exposed.DatabaseFactory
import com.kuki.security.infrastructure.projector.exposed.DatabaseFactory.DatabaseConfig
import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseFactory.init(
        DatabaseConfig(
            driver = this@configureDatabases.environment.config.property("db.driver").getString(),
            url = this@configureDatabases.environment.config.property("db.url").getString(),
            user = this@configureDatabases.environment.config.property("db.user").getString(),
            password = this@configureDatabases.environment.config.property("db.password").getString(),
        )
    )
}
