import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kotlinPluginSerialization)

    alias(libs.plugins.versions)

    alias(libs.plugins.mavenPublish)

    `java-library`
    signing
    java
}

val gitTag: String? by lazy {
    try {
        val stdout = ByteArrayOutputStream()
        rootProject.exec {
            commandLine("git", "describe", "--tags", "--abbrev=0")
            standardOutput = stdout
        }

        stdout.toString().trim()
    } catch(e: Throwable) {
        null
    }
}

val gitHash: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }

    stdout.toString().trim()
}

group = "io.github.blad3mak3r"

version = (gitTag ?: gitHash)

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", Versions.KOTLIN))

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.kotlinxCoroutines)
    implementation(libs.kotlinxSerialization)
    api(libs.caffeine)

    api(libs.slf4jApi)

    testImplementation("junit:junit:4.13.2")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn"
            )
        }

    }

}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

mavenPublishing {
    coordinates("io.github.blad3mak3r", "kotify", "$version")

    pom {
        name.set(project.name)
        description.set("Advanced coroutine-based Spotify API client.")
        url.set("https://github.com/Blad3Mak3r/Kotify")
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/Blad3Mak3r/Kotify/issues")
        }
        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://github.com/Blad3Mak3r/Kotify/LICENSE.txt")
                distribution.set("repo")
            }
        }
        scm {
            url.set("https://github.com/Blad3Mak3r/Kotify")
            connection.set("https://github.com/Blad3Mak3r/Kotify.git")
            developerConnection.set("scm:git:ssh://git@github.com:Blad3Mak3r/Kotify.git")
        }
        developers {
            developer {
                name.set("Juan Luis Caro")
                url.set("https://github.com/Blad3Mak3r")
            }
        }
    }

    publishToMavenCentral(automaticRelease = true)

    signAllPublications()
}
