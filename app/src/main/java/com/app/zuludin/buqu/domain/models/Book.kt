package com.app.zuludin.buqu.domain.models

data class Book(
    val bookId: String,
    val title: String,
    val author: String,
    val cover: String,
    val description: String,
    val totalPages: Int,
    val publisher: String = "",
    val year: Int = 0,
    val isSelected: Boolean = false
)
