import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.4.21"
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions.suppressWarnings = true
compileKotlin.kotlinOptions.jvmTarget = "1.8"

group = "com.thebluehats.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
}

dependencies {
    val kotestVersion = "4.2.0"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.konghq:unirest-java:3.11.04")
    implementation("com.google.inject:guice:4.1.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.mockk:mockk:1.10.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
