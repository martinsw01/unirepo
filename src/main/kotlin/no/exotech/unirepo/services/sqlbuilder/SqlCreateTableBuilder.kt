package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import java.lang.reflect.Field
import javax.persistence.Id

class SqlCreateTableBuilder(val clazz: Class<out BaseEntity>) {
    private val typeNamePairs: MutableList<String> = ArrayList()

    init {
        SqlUtils.traverseFieldsInClass(clazz) {
            val column = SqlUtils.camelToSnakeCase(it.name)
            typeNamePairs.add("$column ${getFieldType(it)}")
        }
    }

    private fun typeNamePairsToString(): String {
        return typeNamePairs.joinToString {it}
    }

    private fun getFieldType(field: Field): String {
        if (field.isAnnotationPresent(Id::class.java))
            return "UUID DEFAULT RANDOM_UUID() NOT NULL"
        return when (field.type) {
            Int::class.java -> "INTEGER"
            String::class.java -> "VARCHAR(250)"
            Boolean::class.java -> "BOOLEAN"
            Class::class.java -> "VARCHAR(250)"
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