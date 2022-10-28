plugins {
    kotlin("jvm") version Versions.KOTLIN
    kotlin("plugin.serialization") version Versions.KOTLIN

    id("org.jetbrains.dokka") version "1.6.0"

    `maven-publish`
    `java-library`
    signing
    java
}

group = "tv.blademaker"
val versionObj = Version(1, 0, 0, "alpha.2")
version = versionObj.toString()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", Versions.KOTLIN))

    implementation(Deps.KTOR_CLIENT_CORE)
    implementation(Deps.KTOR_CLIENT_CIO)
    implementation(Deps.KTOR_CLIENT_CONTENT_NEGOTIATION)
    implementation(Deps.KTOR_SERIALIZATION_JSON)
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_JDK8)
    implementation(Deps.KOTLINX_SERIALIZATION_JSON)
    api(Deps.CAFFEINE)

    api(Deps.SLF4J_API)

    testImplementation("junit:junit:4.13.2")
    testImplementation(Deps.LOGBACK)
}

val dokkaOutputDir = "$buildDir/dokka"

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        this.kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }

    }

    getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
        outputDirectory.set(file(dokkaOutputDir))
    }
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

val mavenCentralRepository = if (versionObj.isSnapshot)
    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
else
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = uri(mavenCentralRepository)

            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("MavenCentral") {
            artifactId = "kotify"
            groupId = project.group as String
            version = project.version as String
            from(components["java"])
            artifact(javadocJar)

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
        }
    }
}

class Version(
    private val major: Int,
    private val minor: Int,
    private val revision: Int,
    val candidate: String? = null
) {
    val isSnapshot = candidate != null

    override fun toString(): String {
        return "$major.$minor.$revision" + if (candidate != null) "-$candidate" else ""
    }
}

val canSign = System.getenv("SIGNING_KEY_ID") != null
if (canSign) {
    signing {
        sign(publishing.publications["MavenCentral"])
    }
}