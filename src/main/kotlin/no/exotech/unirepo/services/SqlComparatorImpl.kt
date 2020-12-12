package no.exotech.unirepo.services

open class SqlComparatorImpl : AndOperation, OrOperator {

    constructor(column: String, comparison: String, value: Any) {
        this.comparators = mutableListOf()
        addToComparators("", column, comparison, value)
    }

    constructor(sqlComparator: SqlComparator) {
        this.comparators = mutableListOf(Pair("", sqlComparator))
    }

    private val comparators: MutableList<Pair<String, Any>>

    fun asList() : List<Pair<String, Any>> {
        return comparators.toList()
    }

    override fun and(column: String,
            comparisonOperator: String,
            value: Any) : AndOperation {
        addToComparators(AND, column, comparisonOperator, value)
        return this
    }

    override fun and(sqlComparator: SqlComparator) : AndOperation {
        comparators.add(Pair(AND, sqlComparator))
        return this
    }

    override fun or(column: String,
           comparisonOperator: String,
           value: Any) : OrOperator {
        addToComparators(OR, column, comparisonOperator, value)
        return this
    }

    override fun or(sqlComparator: SqlComparator) : OrOperator {
        comparators.add(Pair(OR, sqlComparator))
        return this
    }

    private fun addToComparators(type: String, column: String, comparisonOperator: String, value: Any) {
        comparators.add(
                Pair(type, Comparator(column, comparisonOperator, value))
        )
    }

    companion object {
        const val LESS_THAN = "<"
        const val GREATER_THAN = ">"
        const val LESS_THAN_OR_EQUAL = "<="
        const val GREATER_THAN_OR_EQUAL = ">="
        const val NOT_EQUAL = "<>"
        const val EQUAL = "="

        private const val AND = "AND"
        private const val OR = "OR"

        fun compare(column: String,
                    comparisonOperator: String,
                    value: Any) : SqlComparatorImpl {
            return SqlComparatorImpl(column, comparisonOperator, value)
        }

        fun compare(sqlComparator: SqlComparator) : SqlComparatorImpl {
            return SqlComparatorImpl(sqlComparator)
        }
    }

    data class Comparator(
            val column: String,
            val comparisonOperator: String,
            val value: Any
    )
}
