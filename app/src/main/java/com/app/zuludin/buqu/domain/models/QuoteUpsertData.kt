package com.app.zuludin.buqu.domain.models

data class QuoteUpsertData(
    val quote: Quote?,
    val books: List<Book>,
    val categories: List<Category>
)