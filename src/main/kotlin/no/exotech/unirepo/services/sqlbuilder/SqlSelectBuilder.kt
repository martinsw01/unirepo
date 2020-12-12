package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.SqlRequirements
import no.exotech.unirepo.requirements.SqlRequirementsImpl
import no.exotech.unirepo.services.SqlUtils.Companion.getTable

class SqlSelectBuilder {
    companion object {
        @JvmStatic
        fun buildOne(clazz: Class<out Any>, id: Any) : PreparedStatementValues {
            return PreparedStatementValues(
                    "SELECT * FROM ${getTable(clazz)} WHERE id = ?;",
                    listOf(id)
            )
        }
    }

    class Many(private val clazz: Class<out Any>, sqlRequirements: SqlRequirements) {
        private val values = mutableListOf<Any>()
        private val sqlRequirements = sqlRequirements as SqlRequirementsImpl

        fun buildMultiple() : PreparedStatementValues {
            return PreparedStatementValues(
                    """
                        SELECT * FROM ${getTable(clazz)}
                        WHERE
                        ${joinToString(sqlRequirements.asList())}
                    """.trimIndent(),
                    values
            )
        }

        private fun joinToString(requirements: List<Pair<String, Any>>) : String {
            return requirements.joinToString(" ") { (logicalOperator, requirement) ->
                "$logicalOperator ${handleRequirement(requirement)}"
            }
        }

        private fun handleRequirement(requirements: Any) : String {
            return when (requirements) {
                is SqlRequirementsImpl.Requirement -> {
                    values.add(requirements.value)
                    "${requirements.column} ${requirements.comparisonOperator} ?"
                }
                is SqlRequirementsImpl ->
                    "(${joinToString(requirements.asList())})"
                else -> throw IllegalArgumentException(
                        "Should be ${SqlRequirementsImpl::class} " +
                                "or ${SqlRequirementsImpl.Requirement::class}, " +
                                "but was ${requirements.javaClass}")
            }
        }
    }
}