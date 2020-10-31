package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.updates.BaseEntityUpdates
import java.lang.reflect.Field
import javax.persistence.Entity

class SqlService {



    fun createSelectSql(entity: BaseEntity) : String {
        return "SELECT * FROM ${getTable(entity.javaClass)} WHERE id = ${entity.id};"
    }

    fun createTableSql(entity: BaseEntity): String {
        return CreateTable().createSql(entity)
    }

    private fun getTable(clazz: Class<out BaseEntity>): String {
        return clazz.getAnnotation(Entity::class.java).name
    }

    fun createUpdateSql(entity: BaseEntity): String {
        return Update().createSql(entity)
    }

    fun createDeleteSql(entity: BaseEntity): String {
        return """
            DELETE FROM ${getTable(entity.javaClass)}
            WHERE id = ${entity.id};
        """.trimIndent()
    }

    fun createInsertSql(entity: BaseEntity): String {
        return Insert(entity).createSql()
    }


    private data class TypeNamePair(val type: String, val name: String) {
        override fun toString(): String {
            return "$type $name"
        }
    }


    private inner class CreateTable {
        private val typeNamePairs: MutableList<TypeNamePair> = ArrayList()

        private fun addToNamePairs(field: Field) {
            typeNamePairs.add(TypeNamePair(getFieldType(field), field.name))
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

        private fun createTypeNamePairs(clazz: Class<*>) {
            if (clazz.superclass != Object::class.java)
                createTypeNamePairs(clazz.superclass)
            for (field: Field in clazz.declaredFields) {
                addToNamePairs(field)
            }
        }

        fun createSql(entity: BaseEntity): String {
            createTypeNamePairs(entity.javaClass)
            return """
            CREATE TABLE IF NOT EXISTS ${getTable(entity.javaClass)} (
                ${typeNamePairsToString()}
            );
        """.trimIndent()
        }
    }

    private inner class Update {
        fun createSql(entity: BaseEntity): String {
            return """
                UPDATE ${getTable(entity.javaClass)} SET
                ${stringOfUpdates(entity)}
                WHERE id = ${entity.id};
            """.trimIndent()
        }

        private fun stringOfUpdates(entity: BaseEntity): String {
            return BaseEntityUpdates(entity)
                    .stringFields.toString()
                    .replace(Regex("[\\[\\]]"), " ")
        }
    }

    private inner class Insert(val entity: BaseEntity) {
        private val columns: MutableList<String> = ArrayList()
        private val values: MutableList<String> = ArrayList()

        init {
            createColumnValuePairs(entity.javaClass)
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

        private fun createColumnValuePairs(clazz: Class<*>) {
            if (clazz.superclass != Object::class.java)
                createColumnValuePairs(clazz.superclass)
            for (field: Field in clazz.declaredFields)
                addToLists(field)
        }

        private fun addToLists(field: Field) {
            field.trySetAccessible()
            columns.add(field.name)
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