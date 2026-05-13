package com.app.zuludin.buqu.ui.book.detail

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Quote

data class BookDetailState(
    val book: Book? = null,
    val quotes: List<Quote> = emptyList(),
    val isLoading: Boolean = false,
    val fromDatabase: Boolean = true,
)