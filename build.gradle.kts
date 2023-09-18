buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm") version "1.9.10" apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.versions) apply false
    alias(libs.plugins.serialization) apply false
}

allprojects {
    group = "com.kuki"
    version = project.version

    repositories {
        mavenCentral()
    }
    
    apply(plugin = "kotlin")

}

