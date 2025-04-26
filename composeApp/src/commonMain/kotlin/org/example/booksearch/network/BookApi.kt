package org.example.booksearch.network

import org.example.booksearch.presentation.booklist.model.Book

interface BookApi {
    suspend fun searchBooks(query: String, displayIndex: Int, startIndex: Int): List<Book>
}