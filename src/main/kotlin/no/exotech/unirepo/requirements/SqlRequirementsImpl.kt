package no.exotech.unirepo.requirements

open class SqlRequirementsImpl : AndOperation, OrOperator {

    constructor(column: String, comparisonOperator: String, value: Any) {
        this.requirements = mutableListOf()
        addToRequirements("", column, comparisonOperator, value)
    }

    constructor(sqlRequirements: SqlRequirements) {
        this.requirements = mutableListOf(Pair("", sqlRequirements))
    }

    private val requirements: MutableList<Pair<String, Any>>

    fun asList(): List<Pair<String, Any>> {
        return requirements.toList()
    }

    override fun and(column: String,
                     comparisonOperator: String,
                     value: Any): AndOperation {
        addToRequirements(AND, column, comparisonOperator, value)
        return this
    }

    override fun and(sqlRequirements: SqlRequirements): AndOperation {
        requirements.add(Pair(AND, sqlRequirements))
        return this
    }

    override fun or(column: String,
                    comparisonOperator: String,
                    value: Any): OrOperator {
        addToRequirements(OR, column, comparisonOperator, value)
        return this
    }

    override fun or(sqlRequirements: SqlRequirements): OrOperator {
        requirements.add(Pair(OR, sqlRequirements))
        return this
    }

    private fun addToRequirements(type: String, column: String, comparisonOperator: String, value: Any) {
        requirements.add(
                Pair(type, Requirement(column, comparisonOperator, value))
        )
    }

    companion object {
        const val LESS_THAN = "<"
        const val GREATER_THAN = ">"
        const val LESS_THAN_OR_EQUAL = "<="
        const val GREATER_THAN_OR_EQUAL = ">="
        const val NOT_EQUAL = "<>"
        const val EQUALS = "="

        private const val AND = "AND"
        private const val OR = "OR"

        fun require(column: String,
                    comparisonOperator: String,
                    value: Any): SqlRequirementsImpl {
            return SqlRequirementsImpl(column, comparisonOperator, value)
        }

        fun require(sqlRequirements: SqlRequirements): SqlRequirementsImpl {
            return SqlRequirementsImpl(sqlRequirements)
        }
    }

    data class Requirement(
            val column: String,
            val comparisonOperator: String,
            val value: Any
    )
}
