package no.exotech.unirepo.services

import no.exotech.unirepo.entities.BaseEntity
import no.exotech.unirepo.models.PreparedStatementValues
import no.exotech.unirepo.services.sqlbuilder.DefaultSqlBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import javax.persistence.Column
import javax.persistence.Entity

class SqlBuilderCreateTableTest {
    private val sqlBuilder = DefaultSqlBuilder()

    @Test
    internal fun createsCorrectSql() {
        val expectedSql = PreparedStatementValues(
                """
                    CREATE TABLE IF NOT EXISTS test_table1
                    (id UUID DEFAULT RANDOM_UUID() NOT NULL, str1 VARCHAR(255),
                    PRIMARY KEY ( id ));
                """.trimIndent()
        )
        val actualSql = sqlBuilder.createTableSql(TestEntity1::class.java)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectSql_WithOtherTableName() {
        val expectedSql = PreparedStatementValues(
                """
                    CREATE TABLE IF NOT EXISTS test_table2
                    (id UUID DEFAULT RANDOM_UUID() NOT NULL, camel_string VARCHAR(255),
                    PRIMARY KEY ( id ));
                """.trimIndent()
        )
        val actualSql = sqlBuilder.createTableSql(TestEntity2::class.java)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun createsCorrectColumnNameForColumnAnnotation() {
        val expectedSql = PreparedStatementValues(
                """
                    CREATE TABLE IF NOT EXISTS column_annotation_test
                    (id VARCHAR(255), string_two VARCHAR(255),
                    PRIMARY KEY ( id ));
                """.trimIndent()
        )
        val actualSql = sqlBuilder.createTableSql(ColumnAnnotationTest::class.java)
        assertEquals(expectedSql, actualSql)
    }

    @Test
    internal fun throwsException_WhenTypeNotSupported() {
        assertThrows(NotImplementedError::class.java) { sqlBuilder.createTableSql(TestEntity3::class.java) }
    }

    @Entity(name = "test_table1")
    private class TestEntity1(val str1: String) : BaseEntity()

    @Entity(name = "test_table2")
    private class TestEntity2(val camelString: String) : BaseEntity()

    @Entity(name = "test_throws_exception_table3")
    private class TestEntity3(val any: Any) : BaseEntity()

    @Entity(name = "column_annotation_test")
    private class ColumnAnnotationTest(
            @Column(columnDefinition = "VARCHAR(255)")
            private val id: String,
            private val stringTwo: String
    )
}