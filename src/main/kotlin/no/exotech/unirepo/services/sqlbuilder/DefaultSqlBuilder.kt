package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import java.util.UUID

class DefaultSqlBuilder : SqlBuilder {
    override fun <Entity : BaseEntity> createSelectSql(clazz: Class<Entity>, id: UUID): PreparedStatementValues {
        return SqlSelectBuilder.build(clazz, id)
    }

    override fun createTableSql(clazz: Class<out BaseEntity>): PreparedStatementValues {
        return SqlCreateTableBuilder(clazz).build()
    }

    override fun createUpdateSql(entity: BaseEntity): PreparedStatementValues {
        return SqlUpdateBuilder(entity).build()
    }

    override fun createDeleteSql(clazz: Class<out BaseEntity>, id: UUID): PreparedStatementValues {
        return SqlDeleteBuilder.build(clazz, id)
    }

    override fun createInsertSql(entity: BaseEntity): PreparedStatementValues {
        return SqlInsertBuilder(entity).build()
    }
}