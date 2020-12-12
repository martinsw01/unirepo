package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlComparator
import no.exotech.unirepo.services.SqlComparatorImpl
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

    class Many(private val clazz: Class<out Any>, sqlComparator: SqlComparator) {
        private val values = mutableListOf<Any>()
        private val sqlComparator = sqlComparator as SqlComparatorImpl

        fun buildMultiple() : PreparedStatementValues {
            return PreparedStatementValues(
                    """
                        SELECT * FROM ${getTable(clazz)}
                        WHERE
                        ${joinToString(sqlComparator.asList())}
                    """.trimIndent(),
                    values
            )
        }

        private fun joinToString(comparators: List<Pair<String, Any>>) : String {
            return comparators.joinToString(" ") { (logicalOperator, comparator) ->
                "$logicalOperator ${handleComparator(comparator)}"
            }
        }

        private fun handleComparator(comparator: Any) : String {
            return when (comparator) {
                is SqlComparatorImpl.Comparator -> {
                    values.add(comparator.value)
                    "${comparator.column} ${comparator.comparisonOperator} ?"
                }
                is SqlComparatorImpl ->
                    "(${joinToString(comparator.asList())})"
                else -> throw IllegalArgumentException(
                        "Should be ${SqlComparatorImpl::class} " +
                                "or ${SqlComparatorImpl.Comparator::class}, " +
                                "but was ${comparator.javaClass}")
            }
        }
    }
}