package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID
import javax.persistence.Entity

class SqlBuilderDeleteTest {

    val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun createsCorrectSql() {
        val expectedSql = PreparedStatementValues(
                """
                    DELETE FROM test_table1
                    WHERE id = ?;
                """.trimIndent(),
                listOf(UUID(0L, 1L))
        )
        val actualSql = sqlBuilder.createDeleteSql(TestEntity::class.java, UUID(0L, 1L))
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    private class TestEntity(id: UUID) : BaseEntity(id)
}