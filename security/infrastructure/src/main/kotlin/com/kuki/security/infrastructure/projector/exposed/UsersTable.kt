package com.kuki.security.infrastructure.projector.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UsersTable : Table() {
    val id = varchar("uuid", 36)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val isEmailVerified = bool("is_email_verified").default(false)
    val givenName = varchar("given_name", 255).nullable()
    val middleName = varchar("middle_name", 255).nullable()
    val surname = varchar("surname", 255).nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
    val deletedAt = datetime("deleted_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
