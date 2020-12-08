package no.exotech.unirepo.repositories

import no.exotech.unirepo.entities.BaseEntity
import java.util.UUID

interface CrudRepository {
    fun createTable(clazz: Class<out BaseEntity>)
    fun <Entity : BaseEntity> insert(entity: Entity) : UUID
    fun <Entity : BaseEntity> select(entityClazz: Class<Entity>, id: UUID) : Entity
    fun delete(clazz: Class<out BaseEntity>, id: UUID)
}