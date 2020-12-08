package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import java.util.UUID

class SqlDeleteBuilder {
    companion object {
        @JvmStatic
        fun build(clazz: Class<out BaseEntity>, id: UUID): PreparedStatementValues {
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