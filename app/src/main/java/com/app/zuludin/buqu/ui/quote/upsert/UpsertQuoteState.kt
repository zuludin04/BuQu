package com.app.zuludin.buqu.ui.quote.upsert

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Category

data class UpsertQuoteState(
    val field: QuoteInputField = QuoteInputField(),
    val books: List<Book> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isSavingAsImage: Boolean = false,
)

data class QuoteInputField(
    val quote: String = "",
    val image: String = "",
    val bookId: String? = null,
    val categoryId: String = ""
)