pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "devview"

include(":framework")
include(":shared")
include(":security:application")
include(":security:domain")
include(":security:infrastructure")
include(":security:ui")
