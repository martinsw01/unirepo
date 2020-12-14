package no.exotech.unirepo.repositories

import no.exotech.unirepo.requirements.SqlRequirements

interface CrudRepository {
    fun createTable(clazz: Class<out Any>)
    fun <ID : Any> insert(entity: Any, clazz: Class<ID>) : ID   // Returns id
    fun <Entity : Any> select(entityClazz: Class<Entity>, id: Any) : Entity
    fun delete(clazz: Class<out Any>, id: Any)
    fun <Entity : Any> selectMany(clazz: Class<Entity>, requirement: SqlRequirements): List<Entity>
}