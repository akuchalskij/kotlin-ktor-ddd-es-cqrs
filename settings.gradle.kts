pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "security"

include(":framework")
include(":security:application")
include(":security:domain")
include(":security:infrastructure")
include(":security:ui")
