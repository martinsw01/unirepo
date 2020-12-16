package no.exotech.unirepo.requirements

class RequirementPreparer private constructor(sqlRequirements: SqlRequirements) {

    companion object {
        @JvmStatic
        fun prepare(sqlRequirements: SqlRequirements): Pair<List<Any>, String> {
            return RequirementPreparer(sqlRequirements).get()
        }
    }

    private val values = mutableListOf<Any>()
    private val sqlRequirements = sqlRequirements as SqlRequirementsImpl

    private fun get(): Pair<List<Any>, String> {
        return Pair(values, joinToString(sqlRequirements.asList()))
    }

    private fun handleRequirement(requirements: Any): String {
        return when (requirements) {
            is SqlRequirementsImpl.Requirement -> {
                values.add(requirements.value)
                "${requirements.column} ${requirements.comparisonOperator} ?"
            }
            is SqlRequirementsImpl ->
                "(${joinToString(requirements.asList())})"
            else -> throw throwIllegalArgumentException(requirements.javaClass)
        }
    }

    private fun joinToString(requirements: List<Pair<String, Any>>): String {
        return requirements.joinToString(" ") { (logicalOperator, requirement) ->
            "$logicalOperator ${handleRequirement(requirement)}"
        }
    }

    private fun throwIllegalArgumentException(clazz: Class<out Any>): java.lang.IllegalArgumentException {
        return IllegalArgumentException(
                "Should be ${SqlRequirementsImpl::class} " +
                        "or ${SqlRequirementsImpl.Requirement::class}, " +
                        "but was $clazz")
    }
}