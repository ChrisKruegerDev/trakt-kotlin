package app.moviebase.trakt.core

import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.decodeURLPart
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import java.io.File

fun mockHttpClient(responses: Map<String, String>) =
    HttpClient(MockEngine) {
        val jsonFiles = mutableMapOf<String, String>()
        responses.entries.forEach {
            jsonFiles["https://api.trakt.tv/${it.key}"] = it.value
        }
        val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = TraktWebConfig.HOST
            }
        }

        install(ContentNegotiation) {
            val json = JsonFactory.create()
            json(json)
        }

        engine {
            addHandler { request ->
                val url = request.url.toString().decodeURLPart()

                val fileName = jsonFiles[url] ?: error("Unhandled url $url")
                val file = File("./src/jvmTest/resources/trakt/$fileName")
                val content = file.readText()
                respond(content = content, headers = headers)
            }
        }
    }
