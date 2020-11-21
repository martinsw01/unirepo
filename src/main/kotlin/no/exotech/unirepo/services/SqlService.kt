package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.updates.BaseEntityUpdates
import java.lang.reflect.Field
import javax.persistence.Entity

class SqlService {

    companion object {
        @JvmStatic
        fun createSelectSql(entity: BaseEntity) : String {
            return "SELECT * FROM ${getTable(entity.javaClass)} WHERE id = ${entity.id};"
        }

        @JvmStatic
        fun createTableSql(entity: BaseEntity): String {
            return CreateTable(entity).createSql()
        }

        @JvmStatic
        fun createUpdateSql(entity: BaseEntity): String {
            return Update(entity).createSql()
        }

        @JvmStatic
        fun createDeleteSql(entity: BaseEntity): String {
            return """
            DELETE FROM ${getTable(entity.javaClass)}
            WHERE id = ${entity.id};
        """.trimIndent()
        }

        @JvmStatic
        fun createInsertSql(entity: BaseEntity): String {
            return Insert(entity).createSql()
        }

        @JvmStatic
        private fun getTable(clazz: Class<out BaseEntity>): String {
            return clazz.getAnnotation(Entity::class.java).name
        }

        @JvmStatic
        private fun handleFieldsRecursively(clazz: Class<*>, callback: (field: Field) -> Unit ) {
            if (clazz.superclass != Object::class.java)
                handleFieldsRecursively(clazz.superclass, callback)
            for (field: Field in clazz.declaredFields) {
                field.trySetAccessible()
                callback(field)
            }
        }

        private class CreateTable(val entity: BaseEntity) {

            private val typeNamePairs: MutableList<String> = ArrayList()

            init {
                handleFieldsRecursively(entity.javaClass) {
                    typeNamePairs.add("${getFieldType(it)} ${it.name}")
                }
            }

            private fun typeNamePairsToString(): String {
                //replaces square brackets in list of type-name-pair
                return typeNamePairs.toString()
                        .replace(regex = Regex("[\\[\\]]"), replacement = "")
            }

            private fun getFieldType(field: Field): String {
                return when (field.type) {
                    Int::class.java -> "integer"
                    String::class.java -> "varchar(250)"
                    Boolean::class.java -> "boolean"
                    else -> throw NotImplementedError("${field.type} not yet supported")
                }
            }

            fun createSql(): String {
                return """
            CREATE TABLE IF NOT EXISTS ${getTable(entity.javaClass)} (
                ${typeNamePairsToString()}
            );
        """.trimIndent()
            }
        }

        private class Update(val entity: BaseEntity) {
            fun createSql(): String {
                return """
                UPDATE 
                ${getTable(entity.javaClass)} 
                SET
                ${stringOfUpdates()}
                WHERE id = ${entity.id};
            """.trimIndent()
            }

            private fun stringOfUpdates(): String {
                return BaseEntityUpdates(entity)
                        .stringFields.toString()
                        .replace(Regex("[\\[\\]]"), " ")
            }
        }

        private class Insert(val entity: BaseEntity) {
            private val columns: MutableList<String> = ArrayList()
            private val values: MutableList<String> = ArrayList()

            init {
                handleFieldsRecursively(entity.javaClass) {
                    addToLists(it)
                }
            }

            fun createSql(): String{
                return """
                INSERT INTO 
                ${getTable(entity.javaClass)} 
                (${listToString(columns)})
                VALUES
                (${listToString(values)});
            """
            }

            private fun addToLists(field: Field) {
                columns.add(field.name)
                addToValues(field)
            }

            private fun addToValues(field: Field) {
                val value = field.get(entity).toString()
                if (field.type == String::class.java)
                    values.add("'$value'")
                else values.add(value)
            }

            private fun listToString(list: List<String>): String {
                return list.toString().replace(Regex("[\\[\\]]"), " ")
            }
        }
    }
}