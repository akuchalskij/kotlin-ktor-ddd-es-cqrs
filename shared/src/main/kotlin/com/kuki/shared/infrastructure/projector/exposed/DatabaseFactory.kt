package com.kuki.shared.infrastructure.projector.exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(config: DatabaseConfig, vararg tables: Table) {
        val database = Database.connect(
            driver = config.driver,
            url = config.url,
            user = config.user,
            password = config.password
        )

        transaction(database) {
            SchemaUtils.create(*tables)
        }
    }

    data class DatabaseConfig(
        val driver: String,
        val url: String,
        val user: String,
        val password: String
    )
}
