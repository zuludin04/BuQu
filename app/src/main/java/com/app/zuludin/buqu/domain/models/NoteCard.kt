package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.unit.IntSize

data class NoteCard(
    val noteId: String,
    val boardId: String,
    val title: String,
    val posX: Float,
    val posY: Float,
    val size: IntSize = IntSize.Zero,
    val isSelected: Boolean = false,
    val color: String,
)
