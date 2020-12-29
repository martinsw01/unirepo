package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import java.lang.reflect.Field

class SqlInsertBuilder {

    companion object {
        @JvmStatic
        fun build(entities: Iterable<Any>): PreparedStatementValues {
            val clazz = getClass(entities)
            val fields = SqlUtils.getAllFields(clazz)
            val columns = getColumns(fields)
            val values = getValues(entities, fields)
            val questionMarks = formatToQMs(values)

            return PreparedStatementValues(
                    """
                        INSERT INTO ${getTable(clazz)}
                        (${columns.joinToString()})
                        VALUES
                        $questionMarks
                    """.trimIndent(),
                    values.flatten().filterNotNull()
            )
        }

        @JvmStatic
        private fun getColumns(fields: List<Field>): List<String> {
            return fields.map { it.name }
        }

        @JvmStatic
        private fun getValues(entities: Iterable<Any>, fields: List<Field>): List<List<Any?>> {
            return entities.map { entity ->
                fields.map { field ->
                    getValue(entity, field)
                }
            }
        }

        @JvmStatic
        private fun formatToQMs(values: List<List<Any?>>): String {
            return values.joinToString(", ") { row ->
                "(${
                    row.joinToString { value ->
                        value?.let { "?" } ?: "DEFAULT"
                    }
                })"
            }
        }

        @JvmStatic
        private fun getValue(entity: Any, field: Field): Any? {
            field.trySetAccessible()
            return field.get(entity)
        }

        @JvmStatic
        private fun getClass(entities: Iterable<Any>): Class<*> {
            return (entities as List<Any>)[0].javaClass
        }
    }
}