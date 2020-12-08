package no.exotech.unirepo.repositories

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.entitybuilder.DefaultEmptyEntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilder
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import no.exotech.unirepo.services.sqlbuilder.SqlBuilder
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import java.util.UUID

class Repository(
        className: String,
        url: String,
        user: String,
        psw: String,
        private val sqlBuilder: SqlBuilder = DefaultSqlBuilder(),
        private val entityBuilder: EntityBuilder = EntityBuilder(DefaultEmptyEntityBuilder())
) : BaseRepository(className, url, user, psw), CrudRepository {

    override fun createTable(clazz: Class<out BaseEntity>) {
        createPrepStm(sqlBuilder.createTableSql(clazz)) {
            it.execute()
        }
    }

    override fun <Entity : BaseEntity> insert(entity: Entity): UUID {
        return createPrepStm(sqlBuilder.createInsertSql(entity)) {
            it.executeUpdate()
            getId(it)
        }
    }

    override fun <Entity : BaseEntity> select(entityClazz: Class<Entity>, id: UUID): Entity {
        return createPrepStm(sqlBuilder.createSelectSql(entityClazz, id)) {
            val rs = it.executeQuery()
            entityBuilder.createEntitiesOfResultSet(rs, entityClazz)[0]
        }
    }

    override fun delete(clazz: Class<out BaseEntity>, id: UUID) {
        createPrepStm(sqlBuilder.createDeleteSql(clazz, id)) {
            it.execute()
        }
    }

    fun <Entity : BaseEntity> update(entity: Entity) {
        createPrepStm(sqlBuilder.createUpdateSql(entity)) {
            it.executeUpdate()
        }
    }

    private fun <T> createPrepStm(prepStmValues: PreparedStatementValues, callback: (prepStm: PreparedStatement) -> T) : T {
        val prepStm: PreparedStatement =
                connection.prepareStatement(prepStmValues.query, Statement.RETURN_GENERATED_KEYS)

        return actOnPreparedStatement(prepStm) {
            prepStmValues.values.forEachIndexed { index, value ->
                it.setObject(index + 1, value)
            }
            callback(prepStm)
        }
    }

    @Throws(SQLException::class)
    private fun getId(prepStm: PreparedStatement) : UUID {
        return actOnResultSet(prepStm.generatedKeys) {
            if (it.next()) {
                return@actOnResultSet it.getObject(1, UUID::class.java)
            }
            else {
                throw SQLException("The id of was not found. Prepared statement: $prepStm")
            }
        }
    }

    fun dropAllObjects() {
        createPrepStm(PreparedStatementValues("DROP ALL OBJECTS", emptyList())) {}
    }
}