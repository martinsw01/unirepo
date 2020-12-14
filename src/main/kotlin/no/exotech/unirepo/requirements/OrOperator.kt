package no.exotech.unirepo.requirements

interface OrOperator : SqlRequirements {
    fun or(column: String,
           comparisonOperator: String,
           value: Any): OrOperator

    fun or(sqlRequirements: SqlRequirements): OrOperator
}