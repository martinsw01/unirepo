package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.RequirementPreparer
import no.exotech.unirepo.requirements.SqlRequirements
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import no.exotech.unirepo.updates.EntityUpdates

class SqlUpdateBuilder {
    companion object {
        @JvmStatic
        fun build(entity: Any, id: Any): PreparedStatementValues {
            val (columns, values) = EntityUpdates.of(entity)
            return PreparedStatementValues(
                    """
                    UPDATE ${getTable(entity.javaClass)}
                    SET
                    ${columns.joinToString { "$it = ?" }}
                    WHERE id = ?;
                """.trimIndent(),
                    values.plus(id)
            )
        }

        @JvmStatic
        fun build(entity: Any, requirements: SqlRequirements): PreparedStatementValues {
            val (columns, columnValues) = EntityUpdates.of(entity)
            val (requirementValues, requirementsString) = RequirementPreparer.prepare(requirements)

            return PreparedStatementValues(
                    """
                        UPDATE ${getTable(entity.javaClass)}
                        SET
                        ${columns.joinToString { "$it = ?" }}
                        WHERE
                        $requirementsString
                    """.trimIndent(),
                    columnValues.plus(requirementValues)
            )
        }
    }
}