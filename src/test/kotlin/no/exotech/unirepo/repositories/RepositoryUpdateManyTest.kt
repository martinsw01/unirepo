package no.exotech.unirepo.repositories

import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.EQUALS
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.GREATER_THAN
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.NOT_EQUAL
import no.exotech.unirepo.requirements.SqlRequirementsImpl.Companion.require
import no.exotech.unirepo.services.entitybuilder.EmptyEntityBuilder
import no.exotech.unirepo.services.entitybuilder.EntityBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import javax.persistence.Entity

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RepositoryUpdateManyTest {
    private val repository = Repository(
            "org.h2.Driver",
            "jdbc:h2:./testRepotestExotech",
            "dev_test",
            "dev",
            entityBuilder = EntityBuilder(TestEmptyEntityBuilder())
    )

    @BeforeAll
    fun setUp() {
        repository.createTable(TestEntityCar::class.java)
        (2..6).toList()
                .forEach { wheel ->
                    repository.insert(TestEntityCar("brand$wheel", wheel), UUID::class.java)
                }
    }

    @Test
    internal fun updatesWheelsOnOneEntity() {
        val updateEntity = TestEntityCar(wheels = 3)
        val requirements = require("brand", EQUALS, "brand4")

        repository.updateMany(updateEntity, requirements)

        val expectedEntity = TestEntityCar("brand4", 3)
        val actualEntities = repository.selectMany(TestEntityCar::class.java, requirements)

        assertEquals(1, actualEntities.size)
        assertEquals(expectedEntity, actualEntities[0])
    }

    @Test
    internal fun updatesBrandOnThreeEntities() {
        val updateEntity = TestEntityCar(wheels = 3)
        val updateRequirements = require("wheels", GREATER_THAN, 3)
                .or("brand", EQUALS, "brand2")
        val selectRequirements = require("brand", EQUALS, "brand")
                .or(
                        require("wheels", EQUALS, 3)
                                .and("brand", NOT_EQUAL, "brand3")
                )

        repository.updateMany(updateEntity, updateRequirements)

        val comparator = Comparator() { car1: TestEntityCar, car2: TestEntityCar ->
            (car1.brand?.takeLast(1)?.toInt() ?: 0) - (car2.brand?.takeLast(1)?.toInt() ?: 0)
        }
        val expectedEntities = (4..6).map { wheel ->
            TestEntityCar("brand$wheel", 3)
        }
                .plus(TestEntityCar("brand2", 3))
                .sortedWith(comparator)

        val actualEntities = repository.selectMany(TestEntityCar::class.java, selectRequirements)
                .sortedWith(comparator)

        assertEquals(expectedEntities.size, actualEntities.size)
        assertIterableEquals(expectedEntities, actualEntities)
    }

    @Entity(name = "test_entity_car")
    data class TestEntityCar(var brand: String? = null, var wheels: Int? = null, val id: UUID? = null) {
        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (javaClass != other?.javaClass)
                return false

            other as TestEntityCar

            if (brand != other.brand)
                return false
            if (wheels != other.wheels)
                return false

            return true
        }

        override fun hashCode(): Int {
            var result = brand?.hashCode() ?: 0
            result = 31 * result + (wheels ?: 0)
            return result
        }
    }

    class TestEmptyEntityBuilder : EmptyEntityBuilder {
        @Suppress("UNCHECKED_CAST")
        override fun <Entity> build(clazz: Class<Entity>, vararg args: Any): Entity {
            return TestEntityCar(null, 4) as Entity
        }
    }
}