package app.moviebase.trakt.remote

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

fun mockHttpClient(
    responses: Map<String, String>
) = HttpClient(MockEngine) {
    val jsonFiles = mutableMapOf<String, String>()
    responses.entries.forEach {
        jsonFiles["https://api.trakt.tv/${it.key}"] = it.value
    }
    val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    install(JsonFeature) {
        serializer = KotlinxSerializer(buildJson())
    }

    engine {
        addHandler { request ->
            val url = request.url.toString().decodeURLPart()

            val fileName = jsonFiles[url] ?: error("Unhandled url $url")
            val resource = Resource("./src/jvmTest/resources/trakt/$fileName")

            val content = resource.readText()
            respond(content = content, headers = headers)
        }
    }
}
