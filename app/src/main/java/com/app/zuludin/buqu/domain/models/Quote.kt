package com.app.zuludin.buqu.domain.models

data class Quote(
    val quoteId: String,
    val quote: String,
    val author: String,
    val book: String,
    val page: Int,
    val categoryId: String,
    val color: String = "",
    val category: String = ""
)
