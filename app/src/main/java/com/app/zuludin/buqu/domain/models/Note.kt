package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

data class Note(
    val id: String,
    val content: String,
    val color: Color,
    val xPos: Float,
    val yPos: Float,
    val size: IntSize
)
