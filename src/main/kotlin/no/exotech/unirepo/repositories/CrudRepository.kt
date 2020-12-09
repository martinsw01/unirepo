package no.exotech.unirepo.repositories

import java.util.UUID

interface CrudRepository {
    fun createTable(clazz: Class<out Any>)
    fun insert(entity: Any) : UUID
    fun <Entity : Any> select(entityClazz: Class<Entity>, id: UUID) : Entity
    fun delete(clazz: Class<out Any>, id: UUID)
}