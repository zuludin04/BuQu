package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.unit.IntSize

data class Rope(
    val ropeId: String,
    val sourceNoteId: String,
    val targetNoteId: String,
    val boardId: String,
    val sourceX: Float,
    val sourceY: Float,
    val sourceSize: IntSize,
    val targetX: Float,
    val targetY: Float,
    val targetSize: IntSize
)
