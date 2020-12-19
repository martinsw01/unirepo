package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.persistence.Entity

internal class SqlInsertMultipleTest {
    private val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun insertsOneRow() {
        val entity = TestEntity1("string1")
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table
                    (str1)
                    VALUES
                    (?)
                """.trimIndent(),
                listOf("string1")
        )
        val actualSql = sqlBuilder.createInsertSql(listOf(entity))
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun insertsTwoRows() {
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table
                    (str1)
                    VALUES
                    (?), (?)
                """.trimIndent(),
                listOf("string1", "string2")
        )
        val actualSql = sqlBuilder.createInsertSql(
                (1..2).map { TestEntity1("string$it") })
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun insertsThreeRows() {
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table
                    (str1)
                    VALUES
                    (?), (?), (?)
                """.trimIndent(),
                listOf("string1", "string2", "string3")
        )
        val actualSql = sqlBuilder.createInsertSql(
                (1..3).map { TestEntity1("string$it") })
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun insertsTenRows() {
        val range = (1..10)
        val expectedSql = PreparedStatementValues(
                """
                    INSERT INTO test_table
                    (str1)
                    VALUES
                    ${range.joinToString { "(?)" }}
                """.trimIndent(),
                range.map { "string$it" }
        )
        val actualSql = sqlBuilder.createInsertSql(
                range.map { TestEntity1("string$it") })
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table")
    class TestEntity1(val str1: String)
}