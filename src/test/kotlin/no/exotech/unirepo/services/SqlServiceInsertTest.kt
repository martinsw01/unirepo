package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import javax.persistence.Entity

class SqlServiceInsertTest {
    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1(1, "test", false)
        val expectedSql = """
            INSERT INTO test_table 
            (id, str1, bool)
            VALUES (1, 'test', false);
        """.replace(Regex("[\n ]"), "")
        val actualSql = SqlService.createInsertSql(entity)
                .replace(Regex("[\n ]"), "")
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WithOtherArguments() {
        val entity = TestEntity2(1, "test2", true)
        val expectedSql = """
            INSERT INTO test_table2 
            (id, str1, bool)
            VALUES (1, 'test2', true);
        """.replace(Regex("[\n ]"), "")
        val actualSql = SqlService.createInsertSql(entity)
                .replace(Regex("[\n ]"), "")
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table")
    private class TestEntity1(id: Int, val str1: String, val bool: Boolean) : BaseEntity(id)

    @Entity(name = "test_table2")
    private class TestEntity2(id: Int, val str1: String, val bool: Boolean) : BaseEntity(id)
}