package com.app.zuludin.buqu.ui.book.list

import com.app.zuludin.buqu.domain.models.Book

data class BookState(
    val bookDatabase: BookDatabaseState = BookDatabaseState(),
    val bookOnline: BookOnlineState = BookOnlineState(),
    val scope: BookSearchScope = BookSearchScope.Saved,
    val query: String = ""
)

enum class BookSearchScope {
    Saved,
    Online
}

data class BookDatabaseState(
    val isLoading: Boolean = true,
    val books: List<Book> = emptyList()
)

data class BookOnlineState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
)