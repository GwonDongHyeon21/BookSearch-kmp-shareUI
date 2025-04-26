package org.example.booksearch.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.example.booksearch.presentation.booklist.model.Book
import org.example.booksearch.repository.BookSearchRepository

class BookSearchUseCase(private val repository: BookSearchRepository) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchResultText = MutableStateFlow("책을 검색해주세요.")
    val searchResultText: StateFlow<String> = _searchResultText

    private val _isTopBar = MutableStateFlow(true)
    val isTopBar: StateFlow<Boolean> = _isTopBar

    private val _query = MutableStateFlow("")

    private val _displayNumber = MutableStateFlow(10)

    private val _startNumber = MutableStateFlow(1)

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> get() = _books

    private val _isAddBooks = MutableStateFlow(true)
    val isAddBooks: StateFlow<Boolean> = _isAddBooks

    suspend fun searchBooks(query: String) {
        if (_query.value == query) {
            _startNumber.value += _displayNumber.value
        } else {
            _query.value = query
            _books.value = emptyList()
            _startNumber.value = 1
            _isLoading.value = true
        }

        val addBooks =
            repository.searchBooks(
                query,
                _displayNumber.value,
                _startNumber.value
            )
        if (addBooks.isEmpty()) {
            _isAddBooks.value = false
            if (_books.value.isEmpty())
                _searchResultText.value = "검색 결과가 없습니다."
        } else {
            _books.value += addBooks
        }
        _isLoading.value = false
    }

    fun isTopBarFalse() {
        _isTopBar.value = false
    }

    fun isTopBarTrue() {
        _isTopBar.value = true
    }
}