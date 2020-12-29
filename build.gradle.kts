plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")

    implementation("org.reflections:reflections:0.9.12")

    implementation("javax.persistence:javax.persistence-api:2.2")

    implementation("com.h2database:h2:1.4.200")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jar {
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}

group = "no.exotech"
version = "0.1.1-alpha"