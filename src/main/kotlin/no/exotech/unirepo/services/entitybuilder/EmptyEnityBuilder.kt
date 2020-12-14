package no.exotech.unirepo.services.entitybuilder

interface EmptyEntityBuilder {
    fun <Entity> build(clazz: Class<Entity>, vararg args: Any): Entity
}