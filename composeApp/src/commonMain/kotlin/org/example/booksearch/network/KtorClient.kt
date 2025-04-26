package org.example.booksearch.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    private const val BASE_URL = "openapi.naver.com"

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            header("X-Naver-Client-Id", "8udadXvBTnbLX5IgV08X")
            header("X-Naver-Client-Secret", "bN_QTrvvIt")
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
            }
        }
    }
}