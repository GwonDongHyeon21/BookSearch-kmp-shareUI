package org.example.booksearch.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.booksearch.presentation.booklist.BookListScreen
import org.example.booksearch.presentation.component.TopBar
import org.example.booksearch.usecase.BookSearchUseCase

@Composable
fun BookSearchApp(viewModel: BookSearchUseCase) {
    val isLoading by viewModel.isLoading.collectAsState()
    val books by viewModel.books.collectAsState()
    val isTopBar by viewModel.isTopBar.collectAsState()
    val isAddBooks by viewModel.isAddBooks.collectAsState()
    val searchResultText by viewModel.searchResultText.collectAsState()

    var query by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        while (isAddBooks) {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            if (lastVisibleItemIndex != null && lastVisibleItemIndex >= books.size - 1)
                viewModel.searchBooks(query)
            delay(100)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                state = isTopBar,
                onSearch = {
                    CoroutineScope(Dispatchers.Main).launch {
                        query = it
                        viewModel.searchBooks(it)
                        listState.scrollToItem(0)
                    }
                },
                onBack = { viewModel.isTopBarTrue() }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (isLoading)
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            else {
                if (books.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = searchResultText,
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(color = Color.Gray)
                        )
                    }
                } else {
                    BookListScreen(books, isTopBar, listState, isAddBooks, viewModel)
                }
            }
        }
    }
}