package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.Rope

@Composable
fun RopeComponent(rope: Rope, isPreview: Boolean, isSelected: Boolean, curveLine: Boolean = false) {
    val progress = remember { Animatable(0f) }
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    var centerPos by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(rope.ropeId) {
        progress.animateTo(1f, animationSpec = tween(if (isPreview) 0 else 250))
    }

    val start = remember(rope, density) {
        val offset = with(density) {
            Offset(
                rope.sourceSize.width.pxToDp().toPx() / 2,
                rope.sourceSize.height.pxToDp().toPx() / 2
            )
        }
        Offset(rope.sourceX, rope.sourceY) + offset
    }

    val end = remember(rope, density) {
        val offset = with(density) {
            Offset(
                rope.targetSize.width.pxToDp().toPx() / 2,
                rope.targetSize.height.pxToDp().toPx() / 2
            )
        }
        Offset(rope.targetX, rope.targetY) + offset
    }

    val path = remember(start, end, curveLine) {
        Path().apply {
            moveTo(start.x, start.y)
            if (curveLine) {
                cubicTo(
                    x1 = start.x, y1 = (start.y + end.y) / 2,
                    x2 = end.x, y2 = (start.y + end.y) / 2,
                    x3 = end.x, y3 = end.y
                )
            } else {
                lineTo(end.x, end.y)
            }
        }
    }

    val pathMeasure = remember { PathMeasure() }
    val ropeAlpha = if (isPreview) 0.5f else if (isSelected) 1f else 0.8f
    val ropeColor = Color("#${rope.color}".toColorInt()).copy(alpha = ropeAlpha)

    Canvas(modifier = Modifier.fillMaxSize()) {
        pathMeasure.setPath(path, false)
        val outPath = Path()
        pathMeasure.getSegment(0f, pathMeasure.length * progress.value, outPath)

        drawPath(
            path = outPath,
            color = ropeColor,
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )

        if (rope.caption.isNotBlank()) {
            centerPos = pathMeasure.getPosition(pathMeasure.length / 2f)

            val textLayoutResult = textMeasurer.measure(
                text = rope.caption,
                style = TextStyle(
                    fontSize = 12.sp,
                    background = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = centerPos.x - textLayoutResult.size.width / 2f,
                    y = centerPos.y - textLayoutResult.size.height / 2f
                )
            )
        }
    }
}