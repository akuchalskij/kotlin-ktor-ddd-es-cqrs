package com.kuki.security.infrastructure.projector.exposed

import com.kuki.security.infrastructure.eventstore.exposed.DomainMessagesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(config: DatabaseConfig) {
        val database = Database.connect(
            driver = config.driver,
            url = config.url,
            user = config.user,
            password = config.password
        )

        transaction(database) {
            SchemaUtils.create(DomainMessagesTable, UsersTable)
        }
    }

    data class DatabaseConfig(
        val driver: String,
        val url: String,
        val user: String,
        val password: String
    )
}
