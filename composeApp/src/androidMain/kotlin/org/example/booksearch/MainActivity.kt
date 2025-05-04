package org.example.booksearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.booksearch.network.NaverBookApi
import org.example.booksearch.presentation.BookSearchApp
import org.example.booksearch.repository.BookSearchRepository
import org.example.booksearch.usecase.BookSearchUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val useCase = BookSearchUseCase(BookSearchRepository(NaverBookApi()))

        setContent {
            BookSearchApp(useCase)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    BookSearchApp(BookSearchUseCase(BookSearchRepository(NaverBookApi())))
}