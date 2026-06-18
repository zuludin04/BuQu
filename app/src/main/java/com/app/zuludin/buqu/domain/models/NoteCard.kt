package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.unit.IntSize

data class NoteCard(
    val noteId: String,
    val boardId: String,
    val title: String,
    val posX: Float,
    val posY: Float,
    val size: IntSize,
    val isSelected: Boolean = false,
    val color: String,
    val image: String,
    val isUpdate: Boolean = false,
    val isConnected: Boolean = false,
    val status: String = "active",
    val type: NoteType = NoteType.Text
) {
    val centerX get() = posX + size.width / 2
    val centerY get() = posY + size.height / 2
}

enum class NoteType {
    Text,
    Image,
    Quote,
    Book
}
