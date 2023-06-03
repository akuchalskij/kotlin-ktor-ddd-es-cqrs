package com.kuki.security.infrastructure.eventstore.exposed

import com.kuki.framework.domain.Event
import com.kuki.security.infrastructure.koin.jsonSerializer
import com.kuki.security.infrastructure.util.exposed.json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object DomainMessagesTable : Table() {

    val id = varchar("id", 36)
    val aggregateIdentifier = varchar("aggregate_identifier", 36)
    val payload = json<Event>("payload", jsonSerializer)
    val sequenceNumber = long("sequence_number")
    val occurredOn = timestamp("occurred_on")
}
