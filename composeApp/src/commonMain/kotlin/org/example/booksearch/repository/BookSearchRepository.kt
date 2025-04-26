package org.example.booksearch.repository

import org.example.booksearch.network.BookApi
import org.example.booksearch.presentation.booklist.model.Book

class BookSearchRepository(private val api: BookApi) {
    suspend fun searchBooks(query: String, displayIndex: Int, startIndex: Int): List<Book> {
        return api.searchBooks(query, displayIndex, startIndex)
    }
}