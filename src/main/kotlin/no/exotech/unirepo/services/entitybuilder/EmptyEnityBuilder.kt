package no.exotech.unirepo.services.entitybuilder

import no.exotech.unirepo.entities.BaseEntity

interface EmptyEntityBuilder {
    fun <Entity : BaseEntity> build(clazz: Class<Entity>, vararg args: Any) : Entity
}