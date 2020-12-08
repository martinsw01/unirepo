package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID
import javax.persistence.Entity

class SqlBuilderUpdateTest {
    val sqlBuilder = DefaultSqlBuilder()
    
    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity(UUID(0, 9), "string", null)
        val actualSql = sqlBuilder.createUpdateSql(entity)
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    str1 = ?
                    WHERE id = ?;
                """.trimIndent(),
                listOf("string", UUID(0, 9))
        )

        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WhitMultipleUpdates() {
        val entity = TestEntity(UUID(0, 3), "string", "string2")
        val actualSql = sqlBuilder.createUpdateSql(entity)
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    str1 = ?, camel_case = ?
                    WHERE id = ?;
                """.trimIndent(),
                listOf("string", "string2", UUID(0, 3))
        )
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    private class TestEntity(id: UUID, val str1: String?, val camelCase: String?) : BaseEntity(id)
}