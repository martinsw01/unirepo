package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import java.lang.reflect.Field
import java.util.UUID
import javax.persistence.Column

class SqlCreateTableBuilder(val clazz: Class<out Any>) {
    private val typeNamePairs: MutableList<String> = ArrayList()

    init {
        SqlUtils.traverseFieldsInClass(clazz) {
            val column = SqlUtils.camelToSnakeCase(it.name)
            typeNamePairs.add("$column ${getColumnDefinition(it)}")
        }
    }

    private fun typeNamePairsToString(): String {
        return typeNamePairs.joinToString {it}
    }

    private fun getColumnDefinition(field: Field): String {
        if (field.isAnnotationPresent(Column::class.java))
            return field.getAnnotation(Column::class.java).columnDefinition
        return getDefaultColumnDefinition(field)
    }

    private fun getDefaultColumnDefinition(field: Field) : String {
        return when (field.type) {
            UUID::class.java -> "UUID DEFAULT RANDOM_UUID() NOT NULL"
            Int::class.java -> "INTEGER"
            Integer::class.java -> "INTEGER"
            String::class.java -> "VARCHAR(255)"
            Boolean::class.java -> "BOOLEAN"
            Class::class.java -> "VARCHAR(255)"
            else -> throw NotImplementedError("${field.type} not yet supported")
        }
    }

    fun build(): PreparedStatementValues {
        return PreparedStatementValues(
                """
                    CREATE TABLE IF NOT EXISTS ${SqlUtils.getTable(clazz)}
                    (${typeNamePairsToString()},
                    PRIMARY KEY ( id ));
                """.trimIndent()
        )
    }
}