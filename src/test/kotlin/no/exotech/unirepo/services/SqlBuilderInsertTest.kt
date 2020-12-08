package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import javax.persistence.Entity

class SqlBuilderInsertTest {
    val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1("test", false)
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table
                    ( id, str1, bool )
                    VALUES
                    (?, ?, ?);
                """.trimIndent(),
                listOf(entity.id.toString(), "test", "false")
        )
        val actualSql = sqlBuilder.createInsertSql(entity)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WithOtherArguments() {
        val entity = TestEntity2("test2", true)
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table2
                    ( id, str1, bool )
                    VALUES
                    (?, ?, ?);
                """.trimIndent(),
                listOf(entity.id.toString(), "test2", "true")
        )
        val actualSql = sqlBuilder.createInsertSql(entity)
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table")
    private class TestEntity1(val str1: String, val bool: Boolean) : BaseEntity()

    @Entity(name = "test_table2")
    private class TestEntity2(val str1: String, val bool: Boolean) : BaseEntity()
}