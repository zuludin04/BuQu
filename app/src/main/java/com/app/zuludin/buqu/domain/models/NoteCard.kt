package com.app.zuludin.buqu.domain.models

data class NoteCard(
    val noteId: String,
    val boardId: String,
    val title: String,
    val posX: Float,
    val posY: Float
)
