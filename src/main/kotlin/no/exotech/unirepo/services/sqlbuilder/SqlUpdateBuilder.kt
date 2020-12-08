package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import no.exotech.unirepo.updates.EntityUpdates

class SqlUpdateBuilder(val entity: BaseEntity) {
    fun build(): PreparedStatementValues {
        val (columns, values) = EntityUpdates.of(entity)
        val prepStmtValues = listOf(*values.toTypedArray(), entity.id)
        return PreparedStatementValues(
                """
                    UPDATE ${getTable(entity.javaClass)}
                    SET
                    ${columns.joinToString { "$it = ?" }}
                    WHERE id = ?;
                """.trimIndent(),
                prepStmtValues
        )
    }
}