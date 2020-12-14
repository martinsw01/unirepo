package no.exotech.unirepo.repositories

import no.exotech.unirepo.requirements.SqlRequirementsImpl
import no.exotech.unirepo.services.SqlUtils
import no.exotech.unirepo.services.entitybuilder.EmptyEntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import javax.persistence.Entity

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
        val requirement = SqlRequirementsImpl.require(TestEntityPerson::age.name, SqlRequirementsImpl.EQUAL, age)
        val persons = repository.selectMany(TestEntityPerson::class.java, requirement)
        assertEquals(1, persons.size)
        assertEquals(age, persons[0].age)
    }

    @Entity(name = "test_entity_person")
    class TestEntityPerson(var name: String?, var age: Int?, val id: UUID = UUID.randomUUID())

    class TestEmptyEntityBuilder() : EmptyEntityBuilder {
        @Suppress("UNCHECKED_CAST")
        override fun <Entity> build(clazz: Class<Entity>, vararg args: Any): Entity {
            return TestEntityPerson(null, null) as Entity
        }
    }
}