object Deps {
    const val KTOR_CLIENT_CORE = "io.ktor:ktor-client-core:${Versions.KTOR}"
    const val KTOR_CLIENT_CIO = "io.ktor:ktor-client-cio:${Versions.KTOR}"
    const val KTOR_CLIENT_SERIALIZATION = "io.ktor:ktor-client-serialization:${Versions.KTOR}"

    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_JDK8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.COROUTINES}"
    const val KOTLINX_SERIALIZATION_JSON = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}"
}

object Versions {
    const val COROUTINES = "1.5.2"
    const val KOTLIN = "1.6.10"
    const val KOTLIN_SERIALIZATION = "1.3.1"
    const val KTOR = "1.6.6"
}

data class Version(
    val major: Int,
    val minor: Int,
    val revision: Int,
    val classifier: String? = null
) {
    override fun toString(): String {
        return "$major.$minor.$revision" + if (classifier != null) "-$classifier" else ""
    }
}