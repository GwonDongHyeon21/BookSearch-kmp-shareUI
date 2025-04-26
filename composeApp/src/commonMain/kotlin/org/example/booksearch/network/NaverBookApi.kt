package org.example.booksearch.network

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.example.booksearch.network.KtorClient.client
import org.example.booksearch.presentation.booklist.model.Book

class NaverBookApi : BookApi {
    override suspend fun searchBooks(
        query: String,
        displayIndex: Int,
        startIndex: Int
    ): List<Book> {
        val response: String = client.get("v1/search/book.json") {
            url {
                parameter("query", query)
                parameter("display", displayIndex)
                parameter("start", startIndex)
            }
        }.bodyAsText()

        val json = Json.parseToJsonElement(response).jsonObject
        val items = json["items"]?.jsonArray ?: return emptyList()
        val books = items.mapNotNull { item ->
            Book(
                title = item.jsonObject["title"]?.jsonPrimitive?.content ?: "No Title",
                author = item.jsonObject["author"]?.jsonPrimitive?.content ?: "No author",
                imageUrl = item.jsonObject["image"]?.jsonPrimitive?.content ?: "No Image",
            )
        }

        return books
    }
}