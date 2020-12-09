package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID
import javax.persistence.Entity

class SqlBuilderUpdateTest {
    private val sqlBuilder = DefaultSqlBuilder()
    
    @Test
    internal fun createsCorrectSql() {
        val id = UUID(0, 9)
        val entity = TestEntity(id, "string", null)
        val actualSql = sqlBuilder.createUpdateSql(entity, id)
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    str1 = ?
                    WHERE id = ?;
                """.trimIndent(),
                listOf("string", id)
        )

        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WhitMultipleUpdates() {
        val id = UUID(0, 3)
        val entity = TestEntity(id, "string", "string2")
        val actualSql = sqlBuilder.createUpdateSql(entity, id)
        val expectedSql = PreparedStatementValues(
                """
                    UPDATE test_table1
                    SET
                    str1 = ?, camel_case = ?
                    WHERE id = ?;
                """.trimIndent(),
                listOf("string", "string2", id)
        )
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    private class TestEntity(id: UUID, val str1: String?, val camelCase: String?) : BaseEntity(id)
}