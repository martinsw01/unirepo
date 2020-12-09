package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import java.util.UUID

interface SqlBuilder {
    fun createSelectSql(clazz: Class<out Any>, id: UUID) : PreparedStatementValues
    fun createTableSql(clazz: Class<out Any>): PreparedStatementValues
    fun createUpdateSql(entity: Any, id: Any): PreparedStatementValues
    fun createDeleteSql(clazz: Class<out Any>, id: UUID): PreparedStatementValues
    fun createInsertSql(entity: Any): PreparedStatementValues
}