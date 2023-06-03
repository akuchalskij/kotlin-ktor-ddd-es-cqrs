package com.kuki.security.infrastructure.eventstore.exposed

import com.kuki.framework.domain.DomainMessage
import com.kuki.framework.eventstore.EventStore
import com.kuki.framework.eventstore.EventStoreException
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ExposedEventStore : EventStore {

    override suspend fun load(id: String): List<DomainMessage> = transaction {
        DomainMessagesTable
            .select {
                DomainMessagesTable.aggregateIdentifier eq id
            }
            .map { row ->
                row.toDomainMessage()
            }
            .ifEmpty {
                throw EventStoreException.EventStreamNotFound("Not found events for aggregate with id $id")
            }
    }

    override suspend fun append(id: String, events: List<DomainMessage>) {
        transaction {
            DomainMessagesTable
                .select {
                    DomainMessagesTable.aggregateIdentifier eq id and (DomainMessagesTable.sequenceNumber inList events.map { it.sequenceNumber })
                }
                .singleOrNull()
                ?.let { row ->
                    throw EventStoreException.DuplicateVersionException(row.toDomainMessage())
                }


            DomainMessagesTable.batchInsert(events, shouldReturnGeneratedValues = false) { data ->
                this[DomainMessagesTable.id] = data.id
                this[DomainMessagesTable.aggregateIdentifier] = id
                this[DomainMessagesTable.payload] = data.payload
                this[DomainMessagesTable.sequenceNumber] = data.sequenceNumber
                this[DomainMessagesTable.occurredOn] = data.occurredOn
            }
        }
    }

    private suspend fun <T> transaction(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun ResultRow.toDomainMessage() = DomainMessage(
        this[DomainMessagesTable.id],
        this[DomainMessagesTable.payload],
        this[DomainMessagesTable.sequenceNumber],
        this[DomainMessagesTable.occurredOn],
    )
}
