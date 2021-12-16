plugins {
    kotlin("jvm") version Versions.KOTLIN
    kotlin("plugin.serialization") version Versions.KOTLIN apply true
}

group = "tv.blademaker"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", Versions.KOTLIN))

    implementation(Deps.KTOR_CLIENT_CORE)
    implementation(Deps.KTOR_CLIENT_CIO)
    implementation(Deps.KTOR_CLIENT_SERIALIZATION)
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_JDK8)
    implementation(Deps.KOTLINX_SERIALIZATION_JSON)

    api(Deps.SLF4J_API)

    testImplementation("junit:junit:4.13.2")
    testImplementation(Deps.LOGBACK)
}