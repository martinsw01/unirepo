package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils

class SqlDeleteBuilder {
    companion object {
        @JvmStatic
        fun build(clazz: Class<out Any>, id: Any): PreparedStatementValues {
            return PreparedStatementValues(
                    """
                        DELETE FROM ${SqlUtils.getTable(clazz)}
                        WHERE id = ?;
                    """.trimIndent(),
                    listOf(id)
            )
        }
    }
}