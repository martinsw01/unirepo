package no.exotech.unirepo.annotations

import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(value = RUNTIME)
@Target(CONSTRUCTOR)

annotation class SqlConstructor()
