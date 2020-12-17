package no.exotech.unirepo.services

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.EQUALS
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.GREATER_THAN
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.GREATER_THAN_OR_EQUAL_TO
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.LESS_THAN
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.LESS_THAN_OR_EQUAL
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.NOT_EQUAL
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.require
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.persistence.Entity

class SqlBuilderSelectManyTest {
    private val sqlBuilder = DefaultSqlBuilder()

    @Test
    fun createsCorrectSql() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ?
                """.trimIndent(),
                listOf(1)
        )
        val column = TestEntity1::number.name
        val actualSql = sqlBuilder.createSelectManySql(
                TestEntity1::class.java,
                require(column, GREATER_THAN, 1))
        assertEquals(expectedSql, actualSql)
    }

    @Test
    fun createsCorrectSqlWithTwoRequirements() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND number < ?
                """.trimIndent(),
                listOf(1, 3)
        )
        val column = TestEntity1::number.name
        val comparison =
                require(column, GREATER_THAN, 1)
                        .and(column, LESS_THAN, 3)
        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSqlWithManyRequirements() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND number < ? AND number >= ?
                """.trimIndent(),
                listOf(1, 3, 10)
        )
        val column = TestEntity1::number.name
        val comparison =
                require(column, GREATER_THAN, 1)
                        .and(column, LESS_THAN, 3)
                        .and(column, GREATER_THAN_OR_EQUAL_TO, 10)
        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSqlWithNestedRequirements() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND ( number <> ? OR number >= ? OR ( ( string = ? AND number <= ?))) AND number = ?
                """.trimIndent(),
                listOf(1, 3, 10, "hello world", 20, 8)
        )
        val column = TestEntity1::number.name
        val comparison =
                require(column, GREATER_THAN, 1)
                        .and(
                                require(column, NOT_EQUAL, 3)
                                        .or(column, GREATER_THAN_OR_EQUAL_TO, 10)
                                        .or(
                                                require(require("string", EQUALS, "hello world")
                                                        .and(column, LESS_THAN_OR_EQUAL, 20)
                                                ))
                        )
                        .and(column, EQUALS, 8)

        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    class TestEntity1(val number: Int)
}