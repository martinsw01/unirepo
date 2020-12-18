package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import java.lang.reflect.Field

class SqlInsertBuilder {

    companion object {
        @JvmStatic
        fun build(entity: Any): PreparedStatementValues {
            val (columns, values) = createListsOfNamesAndValues(entity)
            val questionMarks = SqlUtils.joinToQMs(values)
            return PreparedStatementValues(
                    """
                        INSERT INTO ${getTable(entity.javaClass)}
                        (${columns.joinToString()})
                        VALUES
                        ($questionMarks);
                    """.trimIndent(),
                    values
            )
        }

        @JvmStatic
        private fun createListsOfNamesAndValues(entity: Any): Pair<List<String>, List<String>> {
            return SqlUtils.getAllFields(entity.javaClass).mapNotNull { field ->
                mapFieldToPair(entity, field)
            }.unzip()
        }

        @JvmStatic
        private fun mapFieldToPair(entity: Any, field: Field): Pair<String, String>? {
            field.trySetAccessible()
            return getValue(entity, field)?.let { value ->
                Pair(field.name, value)
            }
        }

        @JvmStatic
        private fun getValue(entity: Any, field: Field): String? {
            return field.get(entity)?.toString()
        }
    }
}