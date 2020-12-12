package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlComparator

interface SqlBuilder {
    fun createSelectSql(clazz: Class<out Any>, id: Any) : PreparedStatementValues
    fun createSelectManySql(clazz: Class<out Any>, sqlComparator: SqlComparator): PreparedStatementValues
    fun createTableSql(clazz: Class<out Any>): PreparedStatementValues
    fun createUpdateSql(entity: Any, id: Any): PreparedStatementValues
    fun createDeleteSql(clazz: Class<out Any>, id: Any): PreparedStatementValues
    fun createInsertSql(entity: Any): PreparedStatementValues
}