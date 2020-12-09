package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues

class DefaultSqlBuilder : SqlBuilder {
    override fun createSelectSql(clazz: Class<out Any>, id: Any): PreparedStatementValues {
        return SqlSelectBuilder.build(clazz, id)
    }

    override fun createTableSql(clazz: Class<out Any>): PreparedStatementValues {
        return SqlCreateTableBuilder(clazz).build()
    }

    override fun createUpdateSql(entity: Any, id: Any): PreparedStatementValues {
        return SqlUpdateBuilder(entity, id).build()
    }

    override fun createDeleteSql(clazz: Class<out Any>, id: Any): PreparedStatementValues {
        return SqlDeleteBuilder.build(clazz, id)
    }

    override fun createInsertSql(entity: Any): PreparedStatementValues {
        return SqlInsertBuilder(entity).build()
    }
}