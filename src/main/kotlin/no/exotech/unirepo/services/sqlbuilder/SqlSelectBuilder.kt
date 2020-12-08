package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import java.util.UUID

class SqlSelectBuilder {
    companion object {
        @JvmStatic
        fun build(clazz: Class<out BaseEntity>, id: UUID) : PreparedStatementValues {
            return PreparedStatementValues(
                    "SELECT * FROM ${getTable(clazz)} WHERE id = ?;",
                    listOf(id)
            )
        }
    }
}