package com.kuki.shared.infrastructure.util.exposed


import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

inline fun <reified T : Any> Table.json(name: String, json: Json, kSerializer: KSerializer<T> = serializer()): Column<T> =
    registerColumn(name, JsonColumnType(
        serialize = { value ->
            json.encodeToString(kSerializer, value as T)
        },
        deserialize = { string ->
            json.decodeFromString(kSerializer, string)
        }
    ))

class JsonColumnType(
    private val serialize: (Any) -> String,
    private val deserialize: (String) -> Any
) : ColumnType() {
    override fun sqlType() = "JSON"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        super.setParameter(
            stmt,
            index,
            value.let {
                PGobject().apply {
                    this.type = sqlType()
                    this.value = value as String?
                }
            }
        )
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PGobject) {
            return value
        }
        return deserialize(checkNotNull(value.value))
    }

    override fun notNullValueToDB(value: Any): String = serialize(value)
}
