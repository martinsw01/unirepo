package no.exotech.unirepo.models

import java.util.UUID
import javax.persistence.Id

open class BaseEntity(@Id val id: UUID = UUID.randomUUID())