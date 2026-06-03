package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.Rope

@Composable
fun RopeComponent(rope: Rope, isPreview: Boolean, curveLine: Boolean = false) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(rope.ropeId) {
        progress.animateTo(1f, animationSpec = tween(if (isPreview) 0 else 250))
    }

    val sourceSize = rope.sourceSize
    val targetSize = rope.targetSize
    val initialRope = Offset(rope.sourceX, rope.sourceY)
    val targetRope = Offset(rope.targetX, rope.targetY)

    if (curveLine) {
        val pathMeasure = remember { PathMeasure() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val startCenterOffset = Offset(
                sourceSize.width.pxToDp().toPx() / 2, sourceSize.height.pxToDp().toPx() / 2
            )
            val targetCenterOffset = Offset(
                targetSize.width.pxToDp().toPx() / 2, targetSize.height.pxToDp().toPx() / 2
            )

            val start = initialRope + startCenterOffset
            val end = targetRope + targetCenterOffset

            val path = Path().apply {
                moveTo(start.x, start.y)
                cubicTo(
                    x1 = start.x, y1 = (start.y + end.y) / 2,
                    x2 = end.x, y2 = (start.y + end.y) / 2,
                    x3 = end.x, y3 = end.y
                )
            }

            pathMeasure.setPath(path, false)
            val outPath = Path()
            pathMeasure.getSegment(0f, pathMeasure.length * progress.value, outPath)

            drawPath(
                path = outPath,
                color = Color(0xFF7D5260).copy(alpha = if (isPreview) 0.5f else 1f),
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
        }
    } else {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val startCenterOffset = Offset(
                sourceSize.width / 2f, sourceSize.height / 2f
            )
            val targetCenterOffset = Offset(
                targetSize.width / 2f, targetSize.height / 2f
            )

            val start = initialRope + startCenterOffset
            val end = targetRope + targetCenterOffset
            val animatedEnd = start + (end - start) * progress.value

            drawLine(
                color = Color(0xFF7D5260).copy(alpha = if (isPreview) 0.5f else 1f),
                start = start,
                end = animatedEnd,
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
        }
    }
}