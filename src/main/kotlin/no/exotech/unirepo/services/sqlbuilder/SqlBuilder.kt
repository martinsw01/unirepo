package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import java.util.UUID

interface SqlBuilder {
    fun <Entity : BaseEntity> createSelectSql(clazz: Class<Entity>, id: UUID) : PreparedStatementValues
    fun createTableSql(clazz: Class<out BaseEntity>): PreparedStatementValues
    fun createUpdateSql(entity: BaseEntity): PreparedStatementValues
    fun createDeleteSql(clazz: Class<out BaseEntity>, id: UUID): PreparedStatementValues
    fun createInsertSql(entity: BaseEntity): PreparedStatementValues
}