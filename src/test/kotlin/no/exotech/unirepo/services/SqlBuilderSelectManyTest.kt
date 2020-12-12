package no.exotech.unirepo.services

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.EQUAL
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.GREATER_THAN
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.GREATER_THAN_OR_EQUAL
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.LESS_THAN
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.LESS_THAN_OR_EQUAL
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.NOT_EQUAL
import no.exotech.unirepo.services.SqlComparatorImpl.Companion.compare
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
        val actualSql = sqlBuilder.createSelectManySql(
                TestEntity1::class.java,
                compare("number", GREATER_THAN, 1))
        assertEquals(expectedSql, actualSql)
    }

    @Test
    fun createsCorrectSqlWithTwoComparisons() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND number < ?
                """.trimIndent(),
                listOf(1, 3)
        )
        val comparison =
                compare("number", GREATER_THAN, 1)
                .and("number", LESS_THAN, 3)
        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSqlWithManyComparisons() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND number < ? AND number >= ?
                """.trimIndent(),
                listOf(1, 3, 10)
        )
        val comparison =
                compare("number", GREATER_THAN, 1)
                .and("number", LESS_THAN, 3)
                .and("number", GREATER_THAN_OR_EQUAL, 10)
        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSqlWithNestedComparisons() {
        val expectedSql = PreparedStatementValues(
                """
                    SELECT * FROM test_table1
                    WHERE
                     number > ? AND ( number <> ? OR number >= ? OR ( ( string = ? AND number <= ?))) AND number = ?
                """.trimIndent(),
                listOf(1, 3, 10, "hello world", 20, 8)
        )
        val comparison =
                compare("number", GREATER_THAN, 1)
                .and(
                        compare("number", NOT_EQUAL, 3)
                                .or("number", GREATER_THAN_OR_EQUAL, 10)
                                .or(
                                        compare(compare("string", EQUAL, "hello world")
                                                .and("number", LESS_THAN_OR_EQUAL, 20)
                                ))
                )
                .and("number", EQUAL, 8)
        val actualSql = sqlBuilder.createSelectManySql(TestEntity1::class.java, comparison)
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    class TestEntity1
}