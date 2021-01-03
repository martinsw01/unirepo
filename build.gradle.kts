plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    `java-library`
    `maven-publish`
}

repositories {
    jcenter()
}

java {
    withSourcesJar()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")

    implementation("org.reflections:reflections:0.9.12")

    implementation("javax.persistence:javax.persistence-api:2.2")

    testImplementation("com.h2database:h2:1.4.200")

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "no.exotech"
            artifactId = "unirepo"
            version = "0.2.0-alpha"
            from(components["java"])
        }
    }
}