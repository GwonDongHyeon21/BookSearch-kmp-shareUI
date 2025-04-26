package org.example.booksearch.presentation.booklist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.example.booksearch.presentation.bookdetail.BookDetailsScreen
import org.example.booksearch.presentation.booklist.model.Book
import org.example.booksearch.usecase.BookSearchUseCase

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookListScreen(
    books: List<Book>,
    state: Boolean,
    listState: LazyListState,
    isAddBooks: Boolean,
    viewModel: BookSearchUseCase
) {
    val updatedState by rememberUpdatedState(state)
    var screenState by remember { mutableStateOf<Screen>(Screen.List) }

    LaunchedEffect(updatedState) {
        if (updatedState) screenState = Screen.List
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = screenState,
            label = "",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            when (it) {
                is Screen.List -> {
                    LazyColumn(state = listState) {
                        items(books) { book ->
                            BookListItem(
                                book,
                                this@SharedTransitionLayout,
                                this@AnimatedContent
                            ) {
                                viewModel.isTopBarFalse()
                                screenState = Screen.Details(book.imageUrl, book.title)
                            }
                        }
                        if (isAddBooks)
                            item {
                                Box(Modifier.fillMaxWidth()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                    }
                }

                is Screen.Details -> {
                    BookDetailsScreen(
                        it.imageUrl,
                        it.title,
                        this@SharedTransitionLayout,
                        this@AnimatedContent
                    )
                }
            }
        }
    }
}

sealed class Screen {
    data object List : Screen()
    data class Details(val imageUrl: String, val title: String) : Screen()
}