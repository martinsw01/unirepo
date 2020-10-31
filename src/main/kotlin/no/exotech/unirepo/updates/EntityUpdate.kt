package no.exotech.unirepo.updates

data class EntityUpdate(val column: String, val value: String) {
    override fun toString(): String {
        return "$column = $value"
    }
}