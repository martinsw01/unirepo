package no.exotech.unirepo.updates

import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.persistence.Transient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BaseEntityUpdatesTest {

    private lateinit var updates: BaseEntityUpdates


    @Test
    fun doesNotReturnNullFields() {
        val entity = TestNullEntity(9, null, "string2")
        val expectedUpdates = listOf(EntityUpdate("str2", "string2"))

        updates = BaseEntityUpdates(entity)
        assertIterableEquals(expectedUpdates, updates.stringFields)
    }

    @Test
    fun doesNotReturnTransientFields() {
        val entity = TestTransientEntity(9, "transient", "string2")
        val expectedUpdates = listOf(
            EntityUpdate("str2", "string2"))
        updates = BaseEntityUpdates(entity)
        assertIterableEquals(expectedUpdates, updates.stringFields)
    }


    class TestNullEntity(id: Int, val nullStr: String?, val str2: String?) : BaseEntity(id)
    open class TestTransientEntity(id: Int, @Transient val transientStr: String?, val str2: String?) : BaseEntity(id)
    class TestThirdGenEntity(id: Int, str1: String, str2: String, val str3: String) : TestTransientEntity(id, str1, str2)
}