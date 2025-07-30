package app.moviebase.trakt.core

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.pipeline.PipelinePhase

typealias RequestInterceptor = suspend (HttpRequestBuilder) -> Unit

fun HttpClient.interceptRequest(
    phase: PipelinePhase = HttpRequestPipeline.Render,
    interceptor: RequestInterceptor,
) = requestPipeline.intercept(phase) { interceptor(context) }
