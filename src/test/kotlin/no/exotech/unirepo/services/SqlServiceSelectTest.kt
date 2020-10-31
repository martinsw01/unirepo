package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import javax.persistence.Entity

class SqlServiceSelectTest {

    private val sqlService = SqlService()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity1(9, "str")
        val actualSql = sqlService.createSelectSql(entity)
        val expectedSql = "SELECT * FROM test_table1 WHERE id = 9;"
        assertEquals(actualSql, expectedSql)
    }

    @Test
    internal fun createsCorrectSql_ofVariableId() {
        val entity = TestEntity1(3, "str")
        val actualSql = sqlService.createSelectSql(entity)
        val expectedSql = "SELECT * FROM test_table1 WHERE id = 3;"
        assertEquals(actualSql, expectedSql)
    }

    @Test
    internal fun createsCorrectSql_ofVariableTable() {
        val entity = TestEntity2(3, "str")
        val actualSql = sqlService.createSelectSql(entity)
        val expectedSql = "SELECT * FROM test_table2 WHERE id = 3;"
        assertEquals(actualSql, expectedSql)
    }


    @Entity(name = "test_table1")
    class TestEntity1(id: Int, val str: String) : BaseEntity(id)

    @Entity(name = "test_table2")
    class TestEntity2(id: Int, val str: String) : BaseEntity(id)
}