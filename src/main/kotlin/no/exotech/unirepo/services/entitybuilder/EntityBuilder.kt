package no.exotech.unirepo.services.entitybuilder

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.services.SqlUtils
import java.sql.ResultSet
import java.util.UUID
import kotlin.collections.ArrayList

class EntityBuilder(private val emptyEntityBuilder: EmptyEntityBuilder) {

    fun <Entity : BaseEntity> createEntitiesOfResultSet(rs: ResultSet, clazz: Class<Entity>): List<Entity> {
        val entities: MutableList<Entity> = ArrayList()
        while (rs.next()) {
            entities.add(createEntityOfResultSet(rs, clazz))
        }
        return entities
    }

    private fun <Entity : BaseEntity> createEntityOfResultSet(rs: ResultSet, clazz: Class<Entity>): Entity {
        val entity: Entity = emptyEntityBuilder.build(clazz, rs.getObject("id", UUID::class.java))
        SqlUtils.traverseFieldsInClass(clazz) {
            it.set(entity, rs.getObject(it.name, it.type))
        }
        return entity
    }
}