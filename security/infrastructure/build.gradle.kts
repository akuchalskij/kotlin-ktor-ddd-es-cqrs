import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
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

dependencies {
    implementation(project(":framework"))
    implementation(project(":shared"))
    implementation(project(":security:domain"))

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.datetime)

    implementation(libs.koin.core)

    implementation(libs.postgresql)
    implementation(libs.auth0.jwt)

    implementation(libs.bundles.exposed)
    implementation(libs.bundles.kmongo)
    implementation(libs.kreds)

    implementation(libs.logback)

    testImplementation(libs.kotlin.junit)
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

tasks.compileKotlin {
    applyKotlinJvmOptions()
}

tasks.compileTestKotlin {
    applyKotlinJvmOptions()
}

