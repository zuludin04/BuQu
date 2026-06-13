package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
fun ArrowDragHandler(position: Offset, rotation: Float) {
    var handlerPosition by remember { mutableStateOf(position) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    when (rotation) {
        0f -> handlerPosition = handlerPosition.copy(
            x = position.x - size.width - 18,
            y = position.y - (size.height / 2)
        )
        90f -> handlerPosition = handlerPosition.copy(
            x = position.x - (size.width / 2),
            y = position.y - size.height - 18
        )
        180f -> handlerPosition = handlerPosition.copy(
            x = position.x + 18,
            y = position.y - (size.height / 2)
        )
        270f -> handlerPosition = handlerPosition.copy(
            x = position.x - (size.width / 2),
            y = position.y + 18
        )
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(handlerPosition.x.toInt(), handlerPosition.y.toInt()) }
            .onSizeChanged { size = it }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = PhosphorArrowLeft,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation)
        )
    }
}