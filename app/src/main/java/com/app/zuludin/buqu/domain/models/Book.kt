package com.app.zuludin.buqu.domain.models

data class Book(
    val bookId: String,
    val title: String,
    val author: String,
    val cover: String,
    val description: String,
    val totalPages: Int,
    val isSelected: Boolean = false
)
