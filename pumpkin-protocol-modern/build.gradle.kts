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

    // Pumpkin Modules
    implementation(project(":pumpkin-math"))
    implementation(project(":pumpkin-nbt"))
    implementation(project(":pumpkin-protocol-core"))
    implementation(project(":pumpkin-text"))
    implementation(project(":pumpkin-util"))

    // Unit Tests
    testCompile("junit", "junit", "4.12")
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