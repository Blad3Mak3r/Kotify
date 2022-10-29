rootProject.name = "Kotify"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            val caffeine = version("caffeine", "3.1.1")
            val coroutines = version("coroutines", "1.6.4")
            val ktor = version("ktor", "2.1.3")
            val slf4j = version("slf4j", "1.7.32")

            library("caffeine", "com.github.ben-manes.caffeine", "caffeine").versionRef(caffeine)

            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(coroutines)
            library("kotlinx-coroutines-jdk8", "org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8").versionRef(coroutines)

            bundle("kotlinx-coroutines", listOf("kotlinx-coroutines-core", "kotlinx-coroutines-jdk8"))

            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef(ktor)
            library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef(ktor)
            library("ktor-client-content-negotiation", "io.ktor", "ktor-client-content-negotiation").versionRef(ktor)

            bundle("ktor-client", listOf("ktor-client-core", "ktor-client-cio", "ktor-client-content-negotiation"))

            library("ktor-serialization-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef(ktor)

            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef(slf4j)
        }
    }
}