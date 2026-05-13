package com.app.zuludin.buqu.ui.book.edit

data class BookEditFieldState(
    val bookId: String? = null,
    val title: String = "",
    val author: String = "",
    val cover: String = "",
    val description: String = "",
    val totalPages: Int = 0,
    val publisher: String = "",
    val year: Int = 0,
)