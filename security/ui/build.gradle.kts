import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}


val JDK_VERSION = 11
val KOTLIN_VERSION = "1.8"

kotlin {
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(JDK_VERSION)
        )
    }
}

fun <T : KotlinJvmOptions> KotlinCompile<T>.applyKotlinJvmOptions() {
    kotlinOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs = listOf("-Xjsr305=strict")
        allWarningsAsErrors = true
        jvmTarget = JDK_VERSION.toString()
        languageVersion = KOTLIN_VERSION
        apiVersion = KOTLIN_VERSION
    }
}

application {
    mainClass.set("com.kuki.security.ui.http.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":security:infrastructure"))
    implementation(project(":security:application"))
    implementation(project(":security:domain"))

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.server.auth)
    implementation(libs.ktor.server.cors)
    
    implementation(libs.bundles.ktor.client)

    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.registry)

    implementation(libs.bundles.koin)
    
    implementation(libs.bundles.exposed)
    implementation(libs.postgresql)
    implementation(libs.h2)
    
    implementation(libs.logback)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.junit)
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.0")
}

tasks.compileKotlin {
    applyKotlinJvmOptions()
}

tasks.compileTestKotlin {
    applyKotlinJvmOptions()
}
