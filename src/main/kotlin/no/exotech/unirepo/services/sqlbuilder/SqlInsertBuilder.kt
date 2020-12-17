package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import java.lang.reflect.Field

class SqlInsertBuilder(val entity: Any) {
    private val columns: MutableList<String> = ArrayList()
    private val values: MutableList<String> = ArrayList()

    init {
        SqlUtils.traverseFieldsInClass(entity.javaClass) {
            addToListsIfNotNull(it)
        }
    }

    fun build(): PreparedStatementValues {
        val questionMarks = SqlUtils.joinToQMs(values)
        return PreparedStatementValues(
                """
                    INSERT INTO ${getTable(entity.javaClass)}
                    (${listToString(columns)})
                    VALUES
                    ($questionMarks);
                """.trimIndent(),
                values
        )
    }

    private fun addToListsIfNotNull(field: Field) {
        getValue(field)?.also { value ->
            columns.add(field.name)
            values.add(value)
        }
    }

    private fun getValue(field: Field): String? {
        return field.get(entity)?.toString()
    }

    private fun listToString(list: List<String>): String {
        return list.toString().replace(Regex("[\\[\\]]"), " ")
    }
}