package no.exotech.unirepo.updates

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.services.SqlUtils
import java.lang.reflect.Field
import javax.persistence.Transient
import javax.persistence.Id


class EntityUpdates private constructor(private val entity: BaseEntity) {

    companion object {
        @JvmStatic
        fun <Entity : BaseEntity> of(entity: Entity) : Pair<List<String>, List<Any>> {
            val updates = EntityUpdates(entity)
            return Pair(
                    updates.columns.toList(),
                    updates.values.toList()
            )
        }
    }

    private val ignoredAnnotations: List<Class<out Annotation>> =
            listOf(Transient::class.java, Id::class.java)

    private val entityFields: MutableList<Field> = ArrayList()

    private val columns: MutableList<String> = ArrayList()
    private val values: MutableList<Any> = ArrayList()

    init {
        getFieldsOfSuperclass(entity.javaClass)
        createFieldsToUpdate()
    }

    private fun createFieldsToUpdate() {
        for (field: Field in entityFields) {
            addToFields(field)
        }
    }

    private fun addToFields(field: Field) {
        field.trySetAccessible()
        if (fieldShouldUpdate(field)) {
            val column: String = SqlUtils.camelToSnakeCase(field.name)
            val value: String = field.get(entity).toString()
            columns.add(column)
            values.add(value)
        }
    }

    private fun fieldShouldUpdate(field: Field): Boolean {
        if (field.get(entity) == null)
            return false
        for (annotation: Class<out Annotation> in ignoredAnnotations)
            if (field.isAnnotationPresent(annotation)) return false
        return true
    }

    private fun getFieldsOfSuperclass(clazz: Class<*>) {
        SqlUtils.traverseFieldsInClass(clazz) {
            entityFields.add(it)
        }
    }
}