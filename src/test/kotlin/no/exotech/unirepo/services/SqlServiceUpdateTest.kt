package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import javax.persistence.Entity

class SqlServiceUpdateTest {

    private val sqlService = SqlService()

    @Test
    internal fun createsCorrectSql() {
        val entity = TestEntity(9, "string", null)
        val actualSql = sqlService.createUpdateSql(entity)
                .replace(Regex("[\n ]"), "")
        val expectedSql = """
            UPDATE test_table1 SET
            str1 = string
            WHERE id = 9;
        """.replace(Regex("[\n ]"), "")

        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WhitMultipleUpdates() {
        val entity = TestEntity(3, "string", "string2")
        val actualSql = sqlService.createUpdateSql(entity)
                .replace(Regex("[\n ]"), "")
        val expectedSql = """
            UPDATE test_table1 SET
            str1 = string,
            str2 = string2
            WHERE id = 3;
        """.replace(Regex("[\n ]"), "")

        assertEquals(expectedSql, actualSql)
    }

    @Entity(name = "test_table1")
    class TestEntity(id: Int, val str1: String?, val str2: String?) : BaseEntity(id)
}