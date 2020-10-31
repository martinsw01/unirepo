package no.exotech.unirepo.updates

import no.exotech.unirepo.entities.BaseEntity
import java.lang.reflect.Field
import javax.persistence.Transient
import javax.persistence.Id


class BaseEntityUpdates(private val entity: BaseEntity) {

    private val ignoredAnnotations: List<Class<out Annotation>> = listOf(
            Transient::class.java,
            Id::class.java
    )
    private val entityFields: MutableList<Field> = ArrayList()
    val stringFields: MutableList<EntityUpdate> = ArrayList()

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
            val column: String = field.name
            val value: String = (field.get(entity) as Any).toString()
            stringFields.add(EntityUpdate(column, value))
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
        entityFields.addAll(clazz.declaredFields)
        if (clazz.superclass != Object::class.java)
            getFieldsOfSuperclass(clazz.superclass)
    }
}