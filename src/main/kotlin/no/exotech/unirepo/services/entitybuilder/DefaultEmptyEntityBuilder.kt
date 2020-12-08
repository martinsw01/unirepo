package no.exotech.unirepo.services.entitybuilder

import no.exotech.unirepo.annotations.SqlConstructor
import no.exotech.unirepo.entities.BaseEntity
import java.lang.reflect.Constructor
import kotlin.streams.asStream

class DefaultEmptyEntityBuilder : EmptyEntityBuilder {
    override fun <Entity : BaseEntity> build(clazz: Class<Entity>, vararg args: Any): Entity {
        return getConstructor(clazz).newInstance(*args)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <Entity : BaseEntity> getConstructor(clazz: Class<Entity>) : Constructor<Entity> {
        try {
            return clazz.constructors.asSequence().asStream()
                    .filter { it.isAnnotationPresent(SqlConstructor::class.java) }
                    .findAny()
                    .get() as Constructor<Entity>
        }

        catch (e: IllegalAccessException) {
            val rethrow = IllegalAccessException("Cannot create new instance of $clazz because the constructor is private")
            rethrow.initCause(e)
            throw rethrow
        }
        catch (e: NoSuchMethodException) {
            val rethrow = NoSuchMethodException("$clazz has no constructor with no parameters")
            rethrow.initCause(e)
            throw rethrow
        }
        catch (e: NoSuchElementException) {
            val rethrow = NoSuchMethodException("$clazz has no public constructor annotated with @SqlConstructor")
            rethrow.initCause(e)
            throw rethrow
        }
    }
}