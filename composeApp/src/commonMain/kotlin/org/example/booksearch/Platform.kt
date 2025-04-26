package org.example.booksearch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform