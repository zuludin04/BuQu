package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.unit.IntSize

data class Yarn(
    val id: String,
    val xSource: Float,
    val ySource: Float,
    val xTarget: Float,
    val yTarget: Float,
    val sourceNoteId: String,
    val targetNoteId: String,
    val sourceSize: IntSize,
    val targetSize: IntSize
)
