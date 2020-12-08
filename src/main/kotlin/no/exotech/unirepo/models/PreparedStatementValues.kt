package no.exotech.unirepo.models

data class PreparedStatementValues(val query: String, val values: List<Any> = emptyList())
