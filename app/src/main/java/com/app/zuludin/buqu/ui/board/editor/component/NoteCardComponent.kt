package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.app.zuludin.buqu.core.icons.PhosphorLightbulb
import com.app.zuludin.buqu.core.icons.PhosphorLinkSimpleHorizontal
import com.app.zuludin.buqu.core.icons.PhosphorPencil
import com.app.zuludin.buqu.core.utils.darken
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.NoteType
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

    Box(
        modifier = Modifier
            .offset { IntOffset(notePosition.x.roundToInt(), notePosition.y.roundToInt()) }
            .onSizeChanged { onGetSize(it) }
            .graphicsLayer {
                val scaleValue = if (isDragging || note.isSelected) 1.03f else 1f
                scaleX = scaleValue
                scaleY = scaleValue
            }
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
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd()
                    },
                    onDragCancel = { isDragging = false },
                    onDrag = { _, dragAmount ->
                        onPositionChanged(dragAmount)
                    },
                )
            },
    ) {
        when (note.type) {
            NoteType.Text -> TextNote(note, backgroundColor)
            NoteType.Image -> ImageNote(note)
            NoteType.Quote -> QuoteNote(note)
            NoteType.Book -> BookNote(note)
        }

        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 12.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp)
        )
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
                val menuBgColor = MaterialTheme.colorScheme.surfaceVariant
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

                        drawPath(path = path, color = menuBgColor)
                    }

                    Surface(
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = menuBgColor,
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = 8.dp
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
private fun TextNote(note: NoteCard, backgroundColor: Color) {
    Surface(
        modifier = Modifier.widthIn(max = 180.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = backgroundColor.darken(0.4f),
            )
        }
    }
}

@Composable
private fun ImageNote(note: NoteCard) {
    Surface(
        modifier = Modifier.width(160.dp),
        color = Color.White,
        shape = RoundedCornerShape(2.dp),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = note.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuoteNote(note: NoteCard) {
    val parchmentColor = Color(0xFFF5F5DC)
    val splitQuote = note.title.split("-")
    val quote = splitQuote.getOrNull(0) ?: "-"
    val author = splitQuote.getOrNull(1) ?: "Quote"

    Surface(
        modifier = Modifier.widthIn(max = 200.dp),
        color = parchmentColor,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = PhosphorLightbulb,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray.copy(alpha = 0.6f)
            )
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                ),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "— $author",
                style = MaterialTheme.typography.bodySmall,
                modifier = Alignment.End.let { Modifier.padding(start = 24.dp) },
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun BookNote(note: NoteCard) {
    val bookColor = Color(0xFFE8E0D5) // Book-like cover color
    val bookSplit = note.title.split("-")
    val title = bookSplit.getOrNull(1) ?: note.title
    val author = bookSplit.getOrNull(0) ?: "Unknown Author"

    Surface(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp),
        color = bookColor,
        shape = RoundedCornerShape(
            topStart = 2.dp,
            topEnd = 8.dp,
            bottomEnd = 8.dp,
            bottomStart = 2.dp
        ),
        shadowElevation = 4.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = note.image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .background(Color.LightGray)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = Color.Black.copy(alpha = 0.2f),
                border = BorderStroke(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row {
                        Text("Title: ", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            title,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row {
                        Text("Author: ", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            author,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OverflowMenuItem(title: String, icon: ImageVector, onClick: () -> Unit) {
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
