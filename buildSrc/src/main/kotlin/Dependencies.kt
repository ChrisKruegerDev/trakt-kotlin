object Versions {
    val versionMajor = 0
    val versionMinor = 1
    val versionPatch = 4

    val versionName = "$versionMajor.$versionMinor.$versionPatch"

    // Plugins
    val dokka = "1.4.30"
    val nexus = "1.0.0"
    val benManesVersions = "0.43.0"

    // Kotlin
    val kotlin = "1.7.20"
    val coroutines = "1.6.4"
    val kotlinxDatetime = "0.4.0"
    val ktor = "2.1.3"
    val serialization = "1.4.1"
    val kotlinIo = "0.1.16"

    // Testing
    val junitJupiter = "5.9.1"
    val truth = "1.1.3"
    val junit = "4.13.2"
    val mockito = "3.12.4"
    val mockitoKotlin = "3.2.0"
}

object Plugins {
    val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
}

object Libs {

    object Kotlin {
        val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
        val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}"
        val kotlinIo = "org.jetbrains.kotlinx:kotlinx-io:${Versions.kotlinIo}"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}-native-mt"
    }

    object Data {
        val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        val ktorJson = "io.ktor:ktor-client-json:${Versions.ktor}"
        val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        val ktorOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        val ktorSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        val ktorAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    }

    object Testing {
        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        val ktorClientMock = "io.ktor:ktor-client-mock:${Versions.ktor}"
        val truth = "com.google.truth:truth:${Versions.truth}"
        val junit = "junit:junit:${Versions.junit}"
        val jupiter = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiter}"
        val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiter}"
        val mockito = "org.mockito:mockito-inline:${Versions.mockito}"
        val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}"
    }
}
