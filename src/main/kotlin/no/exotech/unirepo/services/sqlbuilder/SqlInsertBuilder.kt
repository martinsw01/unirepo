package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.SqlUtils.Companion.getTable
import java.lang.reflect.Field

class SqlInsertBuilder(val entity: BaseEntity) {
    private val columns: MutableList<String> = ArrayList()
    private val values: MutableList<String> = ArrayList()

    init {
        SqlUtils.traverseFieldsInClass(entity.javaClass) {
            addToLists(it)
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

    private fun addToLists(field: Field) {
        columns.add(field.name)
        addToValues(field)
    }

    private fun addToValues(field: Field) {
        val value = field.get(entity).toString()
        values.add(value)
    }

    private fun listToString(list: List<String>): String {
        return list.toString().replace(Regex("[\\[\\]]"), " ")
    }
}