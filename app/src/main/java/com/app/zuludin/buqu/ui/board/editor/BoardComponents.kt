package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import kotlin.math.roundToInt

@Composable
fun NoteCardComponent(
    note: NoteCard,
    onPositionChanged: (String, Float, Float) -> Unit,
    onSelect: (NoteCard, Offset) -> Unit,
    onGetSize: (IntSize) -> Unit,
    isDraggable: Boolean = true,
    isSelectionMode: Boolean,
    isConnectionMode: Boolean
) {
    var isDragging by remember { mutableStateOf(false) }
    var newOffset by remember { mutableStateOf(Offset(note.posX, note.posY)) }

    val updatedOnPositionChanged by rememberUpdatedState(onPositionChanged)

    Box(modifier = Modifier
        .widthIn(max = 180.dp)
        .onSizeChanged { onGetSize(it) }
        .offset { IntOffset(note.posX.roundToInt(), note.posY.roundToInt()) }
        .graphicsLayer {
            rotationZ = (note.noteId.hashCode() % 6 - 3).toFloat()

            if (isDraggable) {
                val scaleValue =
                    if (isDragging || (isSelectionMode && note.isSelected)) 1.15f else 1f
                scaleX = scaleValue
                scaleY = scaleValue
            }
        }
        .neumorphicShadow(backgroundColor = Color("#${note.color}".toColorInt()))
        .border(
            width = if (isSelectionMode && note.isSelected) 3.dp else 0.dp,
            color = if (isSelectionMode && note.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            shape = RoundedCornerShape(12.dp)
        )
        .padding(16.dp)
        .pointerInput(note.noteId, isSelectionMode, isConnectionMode) {
            detectTapGestures(
                onTap = { tapOffset ->
                    val absoluteTapPos = Offset(
                        newOffset.x + tapOffset.x, newOffset.y + tapOffset.y
                    )
                    onSelect(note, absoluteTapPos)
                    isDragging = false
                })
        }
        .pointerInput(note.noteId, isSelectionMode, isConnectionMode) {
            detectDragGestures(
                onDragStart = { isDragging = true },
                onDragEnd = { isDragging = false },
                onDragCancel = { isDragging = false },
                onDrag = { change, dragAmount ->
                    if (!isSelectionMode && !isConnectionMode) {
                        change.consume()
                        newOffset += dragAmount
                        updatedOnPositionChanged(note.noteId, newOffset.x, newOffset.y)
                    }
                })
        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(12.dp)
                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RopeComponent(rope: Rope) {
    val sourceSize = rope.sourceSize
    val targetSize = rope.targetSize
    val initialRope = Offset(rope.sourceX, rope.sourceY)
    val targetRope = Offset(rope.targetX, rope.targetY)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val startCenterOffset = Offset(
            sourceSize.width.pxToDp().toPx() / 2, sourceSize.height.pxToDp().toPx() / 2
        )
        val targetCenterOffset = Offset(
            targetSize.width.pxToDp().toPx() / 2, targetSize.height.pxToDp().toPx() / 2
        )

        drawLine(
            color = Color(0xFF7D5260),
            start = initialRope + startCenterOffset,
            end = targetRope + targetCenterOffset,
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun GridBackgroundComponent(scale: Float, offset: Offset) {
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
fun OverflowMenuItem(title: String, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background, tonalElevation = 8.dp, shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.clickable { onClick() }) {
            Text(
                title,
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
    }
}