package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.ui.board.editor.SelectedIndicator

@Composable
fun NoteSelectIndicator(indicator: SelectedIndicator) {
    Box(
        modifier = Modifier
            .offset { IntOffset(indicator.position.x.toInt(), indicator.position.y.toInt()) }
            .size(indicator.size.width.pxToDp(), indicator.size.height.pxToDp())
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun ArrowDragHandler(
    position: Offset,
    rotation: Float,
    onCreateRope: (Offset) -> Unit,
    onDragRopeEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .offset { IntOffset(position.x.toInt(), position.y.toInt()) }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDragCancel = {},
                    onDragEnd = { onDragRopeEnd() },
                    onDrag = { _, dragAmount ->
                        onCreateRope(dragAmount)
                    }
                )
            }
    ) {
        Icon(
            imageVector = PhosphorArrowLeft,
            contentDescription = null,
            modifier = Modifier.rotate(rotation)
        )
    }
}