package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.domain.models.Camera

enum class BackgroundType { Line, Dot }

@Composable
fun BoardBackground(type: BackgroundType, camera: Camera) {
    when (type) {
        BackgroundType.Line -> GridBackgroundComponent(camera.zoom, camera.offset)
        BackgroundType.Dot -> DotBackgroundComponent(camera.zoom, camera.offset)
    }
}

@Composable
private fun GridBackgroundComponent(scale: Float, offset: Offset) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 40.dp.toPx()
        val scaledGridSize = gridSize * scale

        val startX = (offset.x % scaledGridSize)
        val startY = (offset.y % scaledGridSize)

        for (x in startX.toInt()..size.width.toInt() step scaledGridSize.toInt()) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.2f),
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
        for (y in startY.toInt()..size.height.toInt() step scaledGridSize.toInt()) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.2f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
private fun DotBackgroundComponent(scale: Float, offset: Offset) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 40.dp.toPx()
        val scaledGridSize = gridSize * scale

        val startX = (offset.x % scaledGridSize)
        val startY = (offset.y % scaledGridSize)

        for (x in startX.toInt()..size.width.toInt() step scaledGridSize.toInt()) {
            for (y in startY.toInt()..size.height.toInt() step scaledGridSize.toInt()) {
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.3f),
                    radius = 2f,
                    center = Offset(x = x.toFloat(), y = y.toFloat())
                )
            }
        }
    }
}