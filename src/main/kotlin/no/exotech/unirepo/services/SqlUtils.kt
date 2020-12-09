package no.exotech.unirepo.services

import java.lang.reflect.Field
import javax.persistence.Entity

class SqlUtils {
    companion object {
        @JvmStatic
        fun camelToSnakeCase(str: String) : String {
            return str.replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
                    .toLowerCase()
        }

        @JvmStatic
        fun traverseFieldsInClass(clazz: Class<*>, callback: (Field) -> Unit) {
            if (clazz.superclass != Object::class.java)
                traverseFieldsInClass(clazz.superclass, callback)
            for (field: Field in clazz.declaredFields) {
                field.trySetAccessible()
                callback(field)
            }
        }

        @JvmStatic
        fun joinToQMs(list: List<String>) : String {
            return list.joinToString { "?" }
        }

        @JvmStatic
        fun getTable(clazz: Class<out Any>): String {
            return clazz.getAnnotation(Entity::class.java).name
        }
    }
}