package no.exotech.unirepo.updates

import no.exotech.unirepo.models.BaseEntity
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.persistence.Transient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EntityUpdatesTest {

    @Test
    fun doesNotReturnNullFields() {
        val entity = TestNullEntity(null, "string2")
        val expectedColumns = listOf("str2")
        val expectedValues = listOf("string2")
        val (actualColumns, actualValues) = EntityUpdates.of(entity)
        assertIterableEquals(expectedColumns, actualColumns)
        assertIterableEquals(expectedValues, actualValues)
    }

    @Test
    fun doesNotReturnTransientFields() {
        val entity = TestTransientEntity("transient", "string2")
        val expectedColumns = listOf("str2")
        val expectedValues = listOf("string2")
        val (actualColumns, actualValues) = EntityUpdates.of(entity)
        assertIterableEquals(expectedColumns, actualColumns)
        assertIterableEquals(expectedValues, actualValues)
    }

    @Test
    fun worksWithGrandChildren() {
        val entity = TestThirdGenEntity("transient", "string2", "string3")
        val expectedColumns = listOf("str2", "str3")
        val expectedValues = listOf("string2", "string3")
        val (actualColumns, actualValues) = EntityUpdates.of(entity)
        assertIterableEquals(expectedColumns, actualColumns)
        assertIterableEquals(expectedValues, actualValues)
    }


    class TestNullEntity(val nullStr: String?, val str2: String?) : BaseEntity()
    open class TestTransientEntity(@Transient val transientStr: String?, val str2: String?) : BaseEntity()
    class TestThirdGenEntity(str1: String, str2: String, val str3: String) : TestTransientEntity(str1, str2)
}