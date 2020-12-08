package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID
import javax.persistence.Entity

class SqlBuilderSelectTest {
    private val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1(UUID(0, 9), "str")
        val actualSql = sqlBuilder.createSelectSql(entity.javaClass, entity.id)
        val expectedSql = PreparedStatementValues(
                "SELECT * FROM test_table1 WHERE id = ?;",
                listOf(UUID(0, 9))
        )
        assertEquals(actualSql, expectedSql)
    }

    @Test
    internal fun createsCorrectSql_ofVariableId() {
        val entity = TestEntity1(UUID(0, 3), "str")
        val actualSql = sqlBuilder.createSelectSql(entity.javaClass, entity.id)
        val expectedSql = PreparedStatementValues(
                "SELECT * FROM test_table1 WHERE id = ?;",
                listOf(UUID(0, 3))
        )
        assertEquals(actualSql, expectedSql)
    }

    @Test
    internal fun createsCorrectSql_ofVariableTable() {
        val entity = TestEntity2(UUID(0, 3), "str")
        val actualSql = sqlBuilder.createSelectSql(entity.javaClass, entity.id)
        val expectedSql = PreparedStatementValues(
                "SELECT * FROM test_table2 WHERE id = ?;",
                listOf(UUID(0 ,3))
        )
        assertEquals(actualSql, expectedSql)
    }


    @Entity(name = "test_table1")
    private class TestEntity1(id: UUID, val str: String) : BaseEntity(id)

    @Entity(name = "test_table2")
    private class TestEntity2(id: UUID, val str: String) : BaseEntity(id)
}