package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column

class SqlCreateTableBuilder {
    companion object {
        @JvmStatic
        fun build(clazz: Class<out Any>): PreparedStatementValues {
            val table = SqlUtils.getTable(clazz)
            val typeNamePairsString = createTypeNamePairs(clazz).joinToString()
            return createPreparedStatementValues(table, typeNamePairsString)
        }

        @JvmStatic
        fun createPreparedStatementValues(table: String, typeNamePairs: String): PreparedStatementValues {
            return PreparedStatementValues(
                    """
                    CREATE TABLE IF NOT EXISTS $table
                    ($typeNamePairs,
                    PRIMARY KEY ( id ));
                """.trimIndent()
            )
        }

        @JvmStatic
        private fun createTypeNamePairs(clazz: Class<out Any>): List<String> {
            return SqlUtils.getAllFields(clazz).map { field ->
                mapFieldToTypeNamePair(field)
            }
        }

        @JvmStatic
        private fun mapFieldToTypeNamePair(field: Field): String {
            val column = SqlUtils.camelToSnakeCase(field.name)
            return "$column ${getColumnDefinition(field)}"
        }

        @JvmStatic
        private fun getColumnDefinition(field: Field): String {
            if (field.isAnnotationPresent(Column::class.java))
                return field.getAnnotation(Column::class.java).columnDefinition
            return getDefaultColumnDefinition(field)
        }

        @JvmStatic
        private fun getDefaultColumnDefinition(field: Field): String {
            return when (field.type) {
                UUID::class.java -> "UUID DEFAULT RANDOM_UUID() NOT NULL"
                Int::class.java -> "INTEGER"
                Integer::class.java -> "INTEGER"
                Double::class.java -> "DOUBLE(10, 3)"
                String::class.java -> "VARCHAR(255)"
                Boolean::class.java -> "BOOLEAN"
                LocalDate::class.java -> "DATE"
                LocalDateTime::class.java -> "DATE"
                Class::class.java -> "VARCHAR(255)"
                else -> throw NotImplementedError("${field.type} not yet supported")
            }
        }
    }
}