
plugins {
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.nexus) apply true
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.ben.manes.versions) apply false
    alias(libs.plugins.spotless) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(findProperty("SONATYPE_USER") as String?)
            password.set(findProperty("SONATYPE_PASSWORD") as String?)
            stagingProfileId.set(findProperty("SONATYPE_STAGING_PROFILE_ID_MOVIEBASE") as String?)
        }
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
