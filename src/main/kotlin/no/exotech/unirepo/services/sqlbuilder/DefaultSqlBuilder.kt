package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.SqlRequirements

class DefaultSqlBuilder : SqlBuilder {
    override fun createSelectSql(clazz: Class<out Any>, id: Any): PreparedStatementValues {
        return SqlSelectBuilder.buildOne(clazz, id)
    }

    override fun createTableSql(clazz: Class<out Any>): PreparedStatementValues {
        return SqlCreateTableBuilder.build(clazz)
    }

    override fun createUpdateSql(entity: Any, id: Any): PreparedStatementValues {
        return SqlUpdateBuilder.build(entity, id)
    }

    override fun createUpdateManySql(entity: Any, sqlRequirements: SqlRequirements): PreparedStatementValues {
        return SqlUpdateBuilder.build(entity, sqlRequirements)
    }

    override fun createDeleteSql(clazz: Class<out Any>, id: Any): PreparedStatementValues {
        return SqlDeleteBuilder.build(clazz, id)
    }

    override fun createInsertSql(entity: Any): PreparedStatementValues {
        return SqlInsertBuilder.build(entity)
    }

    override fun createSelectManySql(clazz: Class<out Any>, sqlRequirements: SqlRequirements): PreparedStatementValues {
        return SqlSelectBuilder.buildMultiple(clazz, sqlRequirements)
    }
}