package no.exotech.unirepo.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR

@Retention(value = RUNTIME)
@Target(CONSTRUCTOR)

annotation class SqlConstructor
