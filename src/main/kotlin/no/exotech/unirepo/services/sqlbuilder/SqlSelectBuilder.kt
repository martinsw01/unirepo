package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.RequirementPreparer
import no.exotech.unirepo.requirements.SqlRequirements
import no.exotech.unirepo.services.SqlUtils.Companion.getTable

class SqlSelectBuilder {
    companion object {
        @JvmStatic
        fun buildOne(clazz: Class<out Any>, id: Any): PreparedStatementValues {
            return PreparedStatementValues(
                    "SELECT * FROM ${getTable(clazz)} WHERE id = ?;",
                    listOf(id)
            )
        }

        @JvmStatic
        fun buildMultiple(clazz: Class<out Any>, sqlRequirements: SqlRequirements?): PreparedStatementValues {
            if (sqlRequirements == null)
                return buildAll(clazz)
            val (values, requirementString) = RequirementPreparer.prepare(sqlRequirements)
            return PreparedStatementValues(
                    """
                        SELECT * FROM ${getTable(clazz)}
                        WHERE
                        $requirementString
                    """.trimIndent(),
                    values
            )
        }

        @JvmStatic
        private fun buildAll(clazz: Class<out Any>) =
            PreparedStatementValues("SELECT * FROM ${getTable(clazz)}")
    }
}