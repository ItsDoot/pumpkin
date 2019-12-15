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

    // Vert.x Service Discovery
    implementation("io.vertx:vertx-service-proxy:3.8.3")
    implementation("io.vertx:vertx-service-discovery:3.8.3")

    // Logging
    implementation("org.slf4j:slf4j-api:1.7.29")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
    implementation("org.apache.logging.log4j:log4j-core:2.12.1")
    implementation("io.github.microutils:kotlin-logging:1.7.8")

    // Fast Collections
    implementation("it.unimi.dsi:fastutil:8.3.0")

    implementation(project(":pumpkin-auth"))
    implementation(project(":pumpkin-event"))
    implementation(project(":pumpkin-math"))
    implementation(project(":pumpkin-nbt"))
    implementation(project(":pumpkin-protocol-modern"))
    implementation(project(":pumpkin-util"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}