package com.kuki.security.infrastructure.projector.exposed

import com.kuki.framework.projector.ProjectionException
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.infrastructure.projector.UserViewRepository
import com.kuki.shared.infrastructure.util.exposed.insertOrUpdate
import com.kuki.shared.infrastructure.util.exposed.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ExposedUserViewRepository : UserViewRepository, CheckUserByEmailInterface {

    override suspend fun existsByEmail(email: Email): UserId? = transaction {
        UsersTable
            .slice(UsersTable.id)
            .select { UsersTable.email eq email.toString() }
            .map { row -> UserId.fromString(row[UsersTable.id]) }
            .singleOrNull()
    }

    override suspend fun findByEmail(email: Email): UserView? = transaction {
        UsersTable
            .select { UsersTable.email eq email.toString() }
            .map { row -> row.toUserView() }
            .singleOrNull()
    }

    override suspend fun save(data: UserView) {
        transaction {
            UsersTable.insertOrUpdate(UsersTable.id) { row ->
                row[id] = data.id
                row[email] = data.email
                row[password] = data.password
                row[isEmailVerified] = data.isEmailVerified
                row[givenName] = data.givenName
                row[middleName] = data.middleName
                row[surname] = surname
                row[createdAt] = data.createdAt
                row[updatedAt] = data.updatedAt
                row[deletedAt] = data.deletedAt
            }
        }
    }

    override suspend fun findById(id: String): UserView = transaction {
        UsersTable.select { UsersTable.id eq id }
            .map { row -> row.toUserView() }
            .singleOrNull()
            ?: throw ProjectionException.ProjectionNotFound("User with $id not found.")
    }

    override suspend fun findAll(): List<UserView> = transaction {
        UsersTable.selectAll().map { row -> row.toUserView() }
    }

    override suspend fun findAll(limit: Long, offset: Long): List<UserView> = transaction {
        UsersTable.selectAll()
            .limit(limit.toInt(), offset = offset)
            .map { row -> row.toUserView() }
    }

    private fun ResultRow.toUserView() = UserView(
        this[UsersTable.id].toString(),
        this[UsersTable.email],
        this[UsersTable.password],
        this[UsersTable.isEmailVerified],
        this[UsersTable.givenName],
        this[UsersTable.middleName],
        this[UsersTable.surname],
        this[UsersTable.createdAt],
        this[UsersTable.updatedAt],
        this[UsersTable.deletedAt],
    )
}
