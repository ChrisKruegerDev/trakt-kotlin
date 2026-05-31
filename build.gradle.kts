import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.ben.manes.versions) apply false
    alias(libs.plugins.maven.publish) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            allWarningsAsErrors.set(false)
        }
    }
}
