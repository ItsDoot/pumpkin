import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "pw.dotdash"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    // Vert.x
    implementation("io.vertx:vertx-core:3.8.3")
    implementation("io.vertx:vertx-lang-kotlin:3.8.3")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:3.8.3")

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.29")
    implementation("io.github.microutils:kotlin-logging:1.7.8")

    // NBT
    implementation(project(":pumpkin-nbt"))

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
}