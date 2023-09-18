package com.kuki.security.ui.http.plugins

import com.kuki.security.infrastructure.projector.exposed.UsersTable
import com.kuki.shared.infrastructure.eventstore.exposed.DomainMessagesTable
import com.kuki.shared.infrastructure.projector.exposed.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabases() {
    val config = DatabaseFactory.DatabaseConfig(
        driver = this@configureDatabases.environment.config.property("db.driver").getString(),
        url = this@configureDatabases.environment.config.property("db.url").getString(),
        user = this@configureDatabases.environment.config.property("db.user").getString(),
        password = this@configureDatabases.environment.config.property("db.password").getString(),
    )

    DatabaseFactory.init(config, DomainMessagesTable, UsersTable,)
}
