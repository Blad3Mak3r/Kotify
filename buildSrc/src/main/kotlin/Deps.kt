object Deps {
    const val KTOR_CLIENT_CORE = "io.ktor:ktor-client-core:${Versions.KTOR}"
    const val KTOR_CLIENT_CIO = "io.ktor:ktor-client-cio:${Versions.KTOR}"
    const val KTOR_CLIENT_CONTENT_NEGOTIATION = "io.ktor:ktor-client-content-negotiation:${Versions.KTOR}"
    const val KTOR_SERIALIZATION_JSON = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}"

    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_JDK8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.COROUTINES}"
    const val KOTLINX_SERIALIZATION_JSON = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}"

    const val SLF4J_API = "org.slf4j:slf4j-api:${Versions.SLF4J_API}"

    const val CAFFEINE = "com.github.ben-manes.caffeine:caffeine:${Versions.CAFFEINE}"

    const val LOGBACK = "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
}

object Versions {
    const val CAFFEINE = "3.1.1"
    const val COROUTINES = "1.6.1"
    const val KOTLIN = "1.6.20"
    const val KOTLIN_SERIALIZATION = "1.3.1"
    const val KTOR = "2.0.0"
    const val SLF4J_API = "1.7.32"
    const val LOGBACK = "1.2.7"
}