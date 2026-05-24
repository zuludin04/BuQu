package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.icons.PhosphorLinkSimpleHorizontal
import com.app.zuludin.buqu.core.icons.PhosphorPencil
import com.app.zuludin.buqu.core.utils.darken
import com.app.zuludin.buqu.domain.models.NoteCard
import java.io.File
import kotlin.math.roundToInt

@Composable
fun NoteCardComponent(
    note: NoteCard,
    isHighlighted: Boolean,
    onPositionChanged: (Offset) -> Unit,
    onSelect: (String) -> Unit,
    onGetSize: (IntSize) -> Unit,
    onDragEnd: () -> Unit,
    onUpdateNote: (NoteCard) -> Unit,
    onConnectNote: (NoteCard) -> Unit,
    scale: Float,
) {
    var isDragging by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }

    val notePosition by rememberUpdatedState(Offset(note.posX, note.posY))
    val cameraZoom by rememberUpdatedState(scale)

    val backgroundColor = Color("#${note.color}".toColorInt()).darken(
        when {
            isHighlighted -> 1.1f
            note.isSelected -> 1f
            else -> 0.95f
        }
    )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .widthIn(max = 180.dp)
            .onSizeChanged { onGetSize(it) }
            .offset { IntOffset(notePosition.x.roundToInt(), notePosition.y.roundToInt()) }
            .graphicsLayer {
                val scaleValue = if (isDragging || note.isSelected) 1.03f else 1f
                scaleX = scaleValue
                scaleY = scaleValue
            }
            .neumorphicShadow(backgroundColor = backgroundColor)
            .padding(16.dp)
            .pointerInput(note.noteId) {
                detectTapGestures(
                    onTap = {
                        onSelect(note.noteId)
                        isDragging = false
                    },
                    onLongPress = {
                        popupPosition = notePosition * cameraZoom
                        showMenu = true
                    },
                )
            }
            .pointerInput(note.noteId) {
                detectDragGestures(onDragStart = { isDragging = true }, onDragEnd = {
                    isDragging = false
                    onDragEnd()
                }, onDragCancel = { isDragging = false }, onDrag = { _, dragAmount ->
                    onPositionChanged(dragAmount)
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
                        model = ImageRequest.Builder(context).data(file).crossfade(true).build(),
                        contentDescription = "Note Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
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

    if (showMenu) {
        Popup(
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    val y = (anchorBounds.top + popupPosition.y) - (popupContentSize.height + 16)
                    val x =
                        anchorBounds.left + popupPosition.x + ((note.size.width * cameraZoom) - popupContentSize.width) / 2
                    return IntOffset(
                        x.toInt(), y.toInt()
                    )
                }
            },
            onDismissRequest = { showMenu = !showMenu },
            content = {
                val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                Box(contentAlignment = Alignment.TopCenter) {
                    Canvas(
                        modifier = Modifier
                            .size(width = 20.dp, height = 10.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        val path = Path().apply {
                            moveTo(size.width / 2f, size.height)
                            lineTo(0f, 0f)
                            lineTo(size.width, 0f)
                            close()
                        }

                        drawPath(path = path, color = backgroundColor)
                    }

                    Surface(
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = backgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            OverflowMenuItem(
                                title = "Edit",
                                icon = PhosphorPencil,
                                onClick = {
                                    onUpdateNote(note)
                                    showMenu = !showMenu
                                },
                            )
                            OverflowMenuItem(
                                title = "Connect",
                                icon = PhosphorLinkSimpleHorizontal,
                                onClick = {
                                    onConnectNote(note)
                                    showMenu = !showMenu
                                },
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun OverflowMenuItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Icon(icon, null)
        Text(
            title,
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}