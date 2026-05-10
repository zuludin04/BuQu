package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.utils.darken
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import java.io.File
import kotlin.math.roundToInt

@Composable
fun NoteCardComponent(
    note: NoteCard,
    scale: Float,
    isHighlighted: Boolean,
    onPositionChanged: (NoteCard) -> Unit,
    onSelect: (NoteCard) -> Unit,
    onGetSize: (IntSize) -> Unit,
    onPopupMenu: (Offset) -> Unit,
    onUpdateNote: (String) -> Unit,
    onChangeContent: (String, String) -> Unit,
    onDragEnd: () -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }

    val updatedOffset by rememberUpdatedState(Offset(note.posX, note.posY))
    val updatedOnPositionChanged by rememberUpdatedState(onPositionChanged)

    val backgroundColor =
        Color("#${note.color}".toColorInt()).darken(
            when {
                isHighlighted -> 1.1f
                note.isSelected || note.isConnected -> 1f
                else -> 0.95f
            }
        )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .widthIn(max = 180.dp)
            .onSizeChanged { onGetSize(it) }
            .offset { IntOffset(note.posX.roundToInt(), note.posY.roundToInt()) }
            .graphicsLayer {
                val scaleValue =
                    if (isDragging || note.isSelected) 1.03f else 1f
                scaleX = scaleValue
                scaleY = scaleValue
            }
            .neumorphicShadow(backgroundColor = backgroundColor)
            .padding(16.dp)
            .pointerInput(note.noteId) {
                detectTapGestures(
                    onTap = {
                        onSelect(note)
                        isDragging = false
                    },
                    onLongPress = { offset ->
                        val popupPos = Offset(note.posX + offset.x, note.posY + offset.y)
                        onPopupMenu(popupPos)
                    },
                    onDoubleTap = { onUpdateNote(note.noteId) }
                )
            }
            .pointerInput(note.noteId) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd()
                    },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()

                        val worldDx = dragAmount.x / scale
                        val worldDy = dragAmount.y / scale

                        val newOffset = updatedOffset + Offset(worldDx, worldDy)
                        val n = note.copy(posX = newOffset.x, posY = newOffset.y)

                        updatedOnPositionChanged(n)
                    }
                )
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(12.dp)
                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (note.image != "") {
                val file = File(note.image)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Transparent
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(file)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Note Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                BasicTextField(
                    value = note.title,
                    onValueChange = { onChangeContent(note.noteId, it) },
                    maxLines = 6,
                    enabled = note.isUpdate
                )
            }
        }
    }
}

@Composable
fun RopeComponent(rope: Rope) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(rope.ropeId) {
        progress.animateTo(1f, animationSpec = tween(500))
    }

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

        val start = initialRope + startCenterOffset
        val end = targetRope + targetCenterOffset
        val animatedEnd = start + (end - start) * progress.value

        drawLine(
            color = Color(0xFF7D5260),
            start = start,
            end = animatedEnd,
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
fun DotBackgroundComponent(scale: Float, offset: Offset) {
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