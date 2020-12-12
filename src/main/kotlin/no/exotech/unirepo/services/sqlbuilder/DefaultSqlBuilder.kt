package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlComparator
import no.exotech.unirepo.services.SqlComparatorImpl

class DefaultSqlBuilder : SqlBuilder {
    override fun createSelectSql(clazz: Class<out Any>, id: Any): PreparedStatementValues {
        return SqlSelectBuilder.buildOne(clazz, id)
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

    override fun createSelectManySql(clazz: Class<out Any>, sqlComparator: SqlComparator): PreparedStatementValues {
        return SqlSelectBuilder.Many(clazz, sqlComparator).buildMultiple()
    }
}