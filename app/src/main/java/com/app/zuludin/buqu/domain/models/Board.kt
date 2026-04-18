package com.app.zuludin.buqu.domain.models

data class Board(
    val boardId: String,
    val name: String,
    val color: String,
    val totalNote: Int = 0
)
