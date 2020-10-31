package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.persistence.Entity

class SqlServiceCreateTableTest {
    private val sqlService = SqlService()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1(5, "string1")
        val expectedSql = """
            CREATE TABLE IF NOT EXISTS test_table1 (
                integer id,
                varchar(250) str1
            );
        """.replace(Regex("[\n ]"), "")
        val actualSql = sqlService.createTableSql(entity).replace(Regex("[\n ]"), "")
        print(actualSql)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WithOtherTableName() {
        val entity = TestEntity2(5, "string1")
        val expectedSql = """
            CREATE TABLE IF NOT EXISTS test_table2 (
                integer id,
                varchar(250) str2
            );
        """.replace(Regex("[\n ]"), "")
        val actualSql = sqlService.createTableSql(entity).replace(Regex("[\n ]"), "")
        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    class TestEntity1(id: Int, val str1: String) : BaseEntity(id)

    @Entity(name = "test_table2")
    class TestEntity2(id: Int, val str2: String) : BaseEntity(id)
}