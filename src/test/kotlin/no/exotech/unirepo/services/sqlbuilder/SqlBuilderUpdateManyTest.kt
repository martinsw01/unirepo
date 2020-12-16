package no.exotech.unirepo.services.sqlbuilder

import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.EQUALS
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.LESS_THAN
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.require
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.persistence.Entity

internal class SqlBuilderUpdateManyTest {
    private val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1(str1 = "string1")
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    str1 = ?
                    WHERE
                     number = ?
                """.trimIndent(),
                listOf("string1", 1)
        )
        val requirement = require("number", EQUALS, 1)
        val actualSql = sqlBuilder.createUpdateManySql(entity, requirement)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSqlWithMultipleRequirements() {
        val entity = TestEntity1(4, "string1")
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    number = ?, str1 = ?
                    WHERE
                     number = ? OR ( str1 = ? AND ( number = ? OR number < ?)) OR str1 = ?
                """.trimIndent(),
                listOf("4", "string1", 1, "string2", 3, 2, "string3")
        )
        val requirements =
                require("number", EQUALS, 1)
                        .or(
                                require("str1", EQUALS, "string2")
                                        .and(
                                                require("number", EQUALS, 3)
                                                        .or("number", LESS_THAN, 2)))
                        .or("str1", EQUALS, "string3")

        val actualSql = sqlBuilder.createUpdateManySql(entity, requirements)
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    private data class TestEntity1(val number: Int? = null, val str1: String? = null)
}