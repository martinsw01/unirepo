package no.exotech.unirepo.services

interface OrOperator : SqlComparator {
    fun or(column: String,
           comparisonOperator: String,
           value: Any) : OrOperator

    fun or(sqlComparator: SqlComparator) : OrOperator
}