plugins {
    java
    kotlin("jvm") version "1.3.60"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.60"
}

subprojects {
    group = "pw.dotdash"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
        kotlinOptions.freeCompilerArgs += "-Xinline-classes"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}