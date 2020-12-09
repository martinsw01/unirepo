package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils.Companion.getTable

class SqlSelectBuilder {
    companion object {
        @JvmStatic
        fun build(clazz: Class<out Any>, id: Any) : PreparedStatementValues {
            return PreparedStatementValues(
                    "SELECT * FROM ${getTable(clazz)} WHERE id = ?;",
                    listOf(id)
            )
        }
    }
}