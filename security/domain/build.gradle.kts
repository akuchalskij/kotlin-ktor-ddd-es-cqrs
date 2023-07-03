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
    api(project(":framework"))

    implementation(libs.logback)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.datetime)

    testImplementation(libs.kotlin.junit)
    testImplementation(libs.kotlin.coroutines.test)
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

