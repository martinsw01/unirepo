package no.exotech.unirepo.repositories

import no.exotech.unirepo.requirements.SqlRequirementsImpl
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.EQUALS
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.GREATER_THAN_OR_EQUAL_TO
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.LESS_THAN
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.entitybuilder.EmptyEntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import java.util.stream.IntStream
import javax.persistence.Entity
import kotlin.streams.asStream
import kotlin.streams.toList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RepositorySelectManyTest {
    private val repository = Repository(
            "org.h2.Driver",
            "jdbc:h2:./testRepotestExotech",
            "dev_test",
            "dev",
            entityBuilder = EntityBuilder(TestEmptyEntityBuilder())
    )

    @BeforeAll
    fun createAndSeedTable() {
        repository.createTable(TestEntityPerson::class.java)
        for (age in 10..18) {
            repository.insert(TestEntityPerson("name$age", age), SqlUtils.getMemberClass(TestEntityPerson::id))
        }
    }

    @Test
    internal fun selectsOneRow() {
        val age = 14
        val requirement = SqlRequirementsImpl.require(TestEntityPerson::age.name, EQUALS, age)
        val persons = repository.selectMany(TestEntityPerson::class.java, requirement)
        assertEquals(1, persons.size)
        assertEquals(age, persons[0].age)
    }

    @Test
    internal fun selectsThreeRows() {
        val requirements = SqlRequirementsImpl.require(TestEntityPerson::age.name, LESS_THAN, 18)
                .and(TestEntityPerson::age.name, GREATER_THAN_OR_EQUAL_TO, 15)
        val expectedPersons = (15..17).map {
            age -> TestEntityPerson("name$age", age)
        }
        val actualPersons = repository.selectMany(TestEntityPerson::class.java, requirements)
        assertEquals(expectedPersons.size, actualPersons.size)
        expectedPersons.forEachIndexed {i, expectedPerson ->
            assertEquals(expectedPerson, actualPersons[i])
        }
    }

    @Entity(name = "test_entity_person")
    data class TestEntityPerson(var name: String?, var age: Int?, val id: UUID = UUID.randomUUID()) {
        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (javaClass != other?.javaClass)
                return false

            other as TestEntityPerson

            if (name != other.name)
                return false
            if (age != other.age)
                return false

            return true
        }

        override fun hashCode(): Int {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (age ?: 0)
            return result
        }
    }

    class TestEmptyEntityBuilder() : EmptyEntityBuilder {
        @Suppress("UNCHECKED_CAST")
        override fun <Entity> build(clazz: Class<Entity>, vararg args: Any): Entity {
            return TestEntityPerson(null, null) as Entity
        }
    }
}