package no.exotech.unirepo.repositories

import no.exotech.unirepo.annotations.SqlConstructor
import no.exotech.unirepo.entities.BaseEntity
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import javax.persistence.Entity


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RepositoryTest {
    private val repository = Repository(
            "org.h2.Driver",
            "jdbc:h2:./testRepotestExotech",
            "dev_test",
            "dev")

    @BeforeAll
    internal fun createTables() {
        repository.createTable(TestEntity1::class.java)
        repository.createTable(TestEntity2::class.java)
        repository.createTable(TestEntity3::class.java)
        repository.createTable(TestEntity4::class.java)
    }

    @Test
    internal fun selectAndCreateCorrectEntity() {
        val id = repository.insert(TestEntity1("Martin"))
        val newEntity1 = repository.select(TestEntity1::class.java, id)
        assertEquals("Martin", newEntity1.name)
        assertEquals(id, newEntity1.id)
    }

    @Test
    internal fun throwsExceptionWhenHasNoConstructor() {
        val id = repository.insert(TestEntity2("Martin"))
        assertThrows(NoSuchMethodException::class.java) {
            repository.select(TestEntity2::class.java, id)
        }
    }

    @Test
    internal fun throwsExceptionWhenIsPrivate() {
        val id = repository.insert(TestEntity3())
        assertThrows(IllegalAccessException::class.java) {
            repository.select(TestEntity3::class.java, id)
        }
    }

    @Test
    internal fun throwsExceptionsWhenAnnotationNotPresent() {
        val id = repository.insert(TestEntity4())
        assertThrows(NoSuchMethodException::class.java) {
            repository.select(TestEntity4::class.java, id)
        }
    }

    @Test
    internal fun deletesEntity() {
        repository.delete(TestEntity1::class.java, UUID(0L, 1L))

        assertThrows(IndexOutOfBoundsException::class.java) {
            repository.select(TestEntity1::class.java, UUID(0L, 1L))
        }
    }

    @Test
    internal fun updatesEntity() {
        val id = repository.insert(TestEntity1("Martin"))
        repository.update(TestEntity1(id, "Jørgen"))
        val actualEntity1 = repository.select(TestEntity1::class.java, id)
        assertEquals("Jørgen", actualEntity1.name)
        assertEquals(id, actualEntity1.id)
    }

    @AfterAll
    internal fun tearDown() {
        repository.dropAllObjects()
    }

    @Entity(name = "test1")
    class TestEntity1 : BaseEntity {
        val name: String?
        constructor(name: String) : super() {
            this.name = name
        }
        constructor(id: UUID, name: String) : super(id) {
            this.name = name
        }
        @SqlConstructor
        constructor(id: UUID) : super(id) {
            name = ""
        }
        override fun equals(other: Any?): Boolean {
            if (other is TestEntity1) {
                return (this.name == other.name) and (this.id == other.id)
            }
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return super.hashCode() + 31 * this.id.hashCode() + 17 * this.name.hashCode()
        }

        override fun toString(): String {
            return "$id, $name"
        }
    }
    @Entity(name = "test2")
    class TestEntity2(val thing: String) : BaseEntity()
    @Entity(name = "test3")
    private class TestEntity3 : BaseEntity {
        val name = "martin"

        @SqlConstructor
        constructor(id: UUID) : super(id)

        constructor() : super()
    }

    @Entity(name = "test4")
    class TestEntity4 : BaseEntity {
        constructor(id: UUID) : super(id)
        constructor() : super()
    }
}