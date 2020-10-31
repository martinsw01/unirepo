package no.exotech.unirepo.entities

import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class BaseEntity(@Id val id: Int)