package no.exotech.unirepo.updates

import no.exotech.unirepo.services.SqlUtils
import java.lang.reflect.Field
import javax.persistence.Id
import javax.persistence.Transient


class EntityUpdates {
    companion object {
        @JvmStatic
        fun of(entity: Any): Pair<List<String>, List<String>> {
            return createFieldsToUpdate(entity)
        }

        @JvmStatic
        private fun createFieldsToUpdate(entity: Any): Pair<List<String>, List<String>> {
            return getAllFields(entity::class.java).mapNotNull { field ->
                mapFieldToPair(entity, field)
            }.unzip()
        }

        @JvmStatic
        private fun mapFieldToPair(entity: Any, field: Field): Pair<String, String>? {
            field.trySetAccessible()
            if (!fieldShouldUpdate(entity, field)) {
                return null
            }
            val column: String = SqlUtils.camelToSnakeCase(field.name)
            val value: String = field.get(entity).toString()
            return Pair(column, value)
        }

        @JvmStatic
        private fun fieldShouldUpdate(entity: Any, field: Field): Boolean {
            if (field.get(entity) == null)
                return false
            for (annotation: Class<out Annotation> in ignoredAnnotations)
                if (field.isAnnotationPresent(annotation)) return false
            return true
        }

        @JvmStatic
        private fun getAllFields(clazz: Class<*>): List<Field> {
            val classes = arrayOf(*clazz.getSuperClasses())
            return classes.flatMap { it.declaredFields.asIterable() }
        }

        @JvmStatic
        private fun Class<*>.getSuperClasses(): Array<Class<*>>{
            if (superclass == Object::class.java) {
                return arrayOf(this)
            }
            return arrayOf(*superclass.getSuperClasses(), this)
        }

        @JvmStatic
        private val ignoredAnnotations: List<Class<out Annotation>> =
                listOf(Transient::class.java, Id::class.java)
    }
}