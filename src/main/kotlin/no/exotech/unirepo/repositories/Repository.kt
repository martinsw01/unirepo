package no.exotech.unirepo.repositories

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.SqlRequirements
import no.exotech.unirepo.services.entitybuilder.DefaultEmptyEntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilderImp
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import no.exotech.unirepo.services.sqlbuilder.SqlBuilder
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class Repository(
        className: String,
        url: String,
        user: String,
        psw: String,
        private val sqlBuilder: SqlBuilder = DefaultSqlBuilder(),
        private val entityBuilder: EntityBuilder = EntityBuilderImp(DefaultEmptyEntityBuilder())
) : BaseRepository(className, url, user, psw), CrudRepository {

    override fun createTable(clazz: Class<out Any>) {
        createPrepStm(sqlBuilder.createTableSql(clazz)) {
            it.execute()
        }
    }

    override fun <ID : Any> insert(entity: Any, clazz: Class<ID>): ID {
        return createPrepStm(sqlBuilder.createInsertSql(entity)) {
            it.executeUpdate()
            getId(it, clazz)
        }
    }

    override fun <Entity : Any> select(entityClazz: Class<Entity>, id: Any): Entity {
        return createPrepStm(sqlBuilder.createSelectSql(entityClazz, id)) {
            val rs = it.executeQuery()
            entityBuilder.createEntitiesOfResultSet(rs, entityClazz)[0]
        }
    }

    override fun <Entity : Any> selectMany(clazz: Class<Entity>, requirement: SqlRequirements): List<Entity> {
        return createPrepStm(sqlBuilder.createSelectManySql(clazz, requirement)) {
            val rs = it.executeQuery()
            entityBuilder.createEntitiesOfResultSet(rs, clazz)
        }
    }

    override fun delete(clazz: Class<out Any>, id: Any) {
        createPrepStm(sqlBuilder.createDeleteSql(clazz, id)) {
            it.execute()
        }
    }

    override fun update(entity: Any, id: Any) {
        createPrepStm(sqlBuilder.createUpdateSql(entity, id)) {
            it.executeUpdate()
        }
    }

    override fun updateMany(entity: Any, requirements: SqlRequirements) {
        createPrepStm(sqlBuilder.createUpdateManySql(entity, requirements)) {
            it.executeUpdate()
        }
    }

    private fun <T> createPrepStm(prepStmValues: PreparedStatementValues, callback: (prepStm: PreparedStatement) -> T): T {
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
    private fun <ID : Any> getId(prepStm: PreparedStatement, clazz: Class<ID>): ID {
        return actOnResultSet(prepStm.generatedKeys) {
            if (it.next()) {
                return@actOnResultSet it.getObject(1, clazz)
            } else {
                throw SQLException("The id of was not found. Prepared statement: $prepStm")
            }
        }
    }

    fun dropAllObjects() {
        createPrepStm(PreparedStatementValues("DROP ALL OBJECTS")) {}
    }
}