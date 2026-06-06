package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.geometry.Offset
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
    val targetSize: IntSize,
    val status: String = "active"
) {
    fun middlePoint(): Offset {
        val sourceSize = sourceSize
        val targetSize = targetSize
        val initialRope = Offset(sourceX, sourceY)
        val targetRope = Offset(targetX, targetY)
        val startCenterOffset = Offset(
            sourceSize.width / 2f, sourceSize.height / 2f
        )
        val targetCenterOffset = Offset(
            targetSize.width / 2f, targetSize.height / 2f
        )

        val start = initialRope + startCenterOffset
        val end = targetRope + targetCenterOffset
        val middle = start + (end - start) * 0.5f

        return middle
    }
}
