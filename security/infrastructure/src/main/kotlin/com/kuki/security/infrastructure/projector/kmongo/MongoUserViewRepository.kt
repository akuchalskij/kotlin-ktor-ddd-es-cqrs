package com.kuki.security.infrastructure.projector.kmongo

import com.kuki.framework.projector.ProjectionException
import com.kuki.security.domain.repository.CheckUserByEmailInterface
import com.kuki.security.domain.valueobject.Email
import com.kuki.security.domain.valueobject.UserId
import com.kuki.security.infrastructure.projector.UserView
import com.kuki.security.infrastructure.projector.UserViewRepository
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.commitTransactionAndAwait
import org.litote.kmongo.eq

@Suppress("unused")
class MongoUserViewRepository(
    private val database: String,
    private val client: CoroutineClient
) : UserViewRepository, CheckUserByEmailInterface {

    override suspend fun existsByEmail(email: Email): UserId? =
        client.getDatabase(database)
            .getCollection<UserDocument>()
            .findOne(UserDocument::email eq email.toString())
            ?.let { UserId.fromString(it.id) }

    override suspend fun findByEmail(email: Email): UserView? = client.getDatabase(database)
        .getCollection<UserDocument>()
        .findOne(UserDocument::email eq email.toString())
        ?.toUserView()

    override suspend fun save(data: UserView) {
        client.transaction {
            client.getDatabase(database)
                .getCollection<UserDocument>()
                .insertOne(
                    UserDocument(
                        id = data.id,
                        email = data.email,
                        password = data.password,
                        isEmailVerified = data.isEmailVerified,
                        givenName = data.givenName,
                        middleName = data.middleName,
                        surname = data.surname,
                        createdAt = data.createdAt,
                        updatedAt = data.updatedAt,
                        deletedAt = data.deletedAt
                    )
                )
        }
    }

    override suspend fun findById(id: String): UserView = client.getDatabase(database)
        .getCollection<UserDocument>()
        .findOne(UserDocument::id eq id)
        ?.toUserView()
        ?: throw ProjectionException.ProjectionNotFound("User with $id not found.")

    override suspend fun findAll(): List<UserView> = client.getDatabase(database)
        .getCollection<UserDocument>()
        .find()
        .toList()
        .map { col -> col.toUserView() }

    override suspend fun findAll(limit: Long, offset: Long): List<UserView> = client.getDatabase(database)
        .getCollection<UserDocument>()
        .find()
        .skip(offset.toInt())
        .limit(limit.toInt())
        .toList()
        .map { col -> col.toUserView() }

    private fun UserDocument.toUserView() = UserView(
        id,
        email,
        password,
        isEmailVerified,
        givenName,
        middleName,
        surname,
        createdAt,
        updatedAt,
        deletedAt,
    )

    private suspend fun CoroutineClient.transaction(block: suspend () -> Unit) {
        startSession().use { session ->
            session.startTransaction()
            block()
            session.commitTransactionAndAwait()
        }
    }
}
