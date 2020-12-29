package no.exotech.unirepo.services.entitybuilder

import java.sql.ResultSet

interface EntityBuilder {
    fun <T> createEntitiesOfResultSet(rs: ResultSet, clazz: Class<T>): List<T>
}
