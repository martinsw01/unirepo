package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import javax.persistence.Entity

class SqlServiceDeleteTest {
    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity(1)
        val expectedSql = """
            DELETE FROM test_table1
            WHERE id = 1;
        """.replace(Regex("[\n ]"), "")
        val actualSql = SqlService.createDeleteSql(entity)
                .replace(Regex("[\n ]"), "")
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    private class TestEntity(id: Int) : BaseEntity(id)
}