package no.exotech.unirepo.requirements

interface AndOperation : SqlRequirements {
    fun and(column: String,
            comparisonOperator: String,
            value: Any): AndOperation

    fun and(sqlRequirements: SqlRequirements): AndOperation
}