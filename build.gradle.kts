import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version Versions.KOTLIN
    kotlin("plugin.serialization") version Versions.KOTLIN

    id("org.jetbrains.dokka") version "1.9.20"
    id("com.github.ben-manes.versions") version Versions.VERSIONS
    id("com.vanniktech.maven.publish") version "0.33.0"

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

    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.serialization.json)
    implementation(libs.bundles.kotlinx.coroutines)
    api(libs.caffeine)

    api(libs.slf4j.api)

    testImplementation("junit:junit:4.13.2")
}

val dokkaOutputDir = "${layout.buildDirectory}/dokka"

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

val canSign = System.getenv("SIGNING_KEY_ID") != null
if (canSign) {
    signing {
        sign(publishing.publications["MavenCentral"])
    }
}
