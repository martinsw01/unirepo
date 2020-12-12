package no.exotech.unirepo.services

interface AndOperation : SqlComparator {
    fun and(column: String,
            comparisonOperator: String,
            value: Any) : AndOperation

    fun and(sqlComparator: SqlComparator) : AndOperation
}