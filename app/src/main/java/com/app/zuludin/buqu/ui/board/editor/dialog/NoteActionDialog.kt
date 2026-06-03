package com.app.zuludin.buqu.ui.board.editor.dialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.app.zuludin.buqu.core.icons.PhosphorLinkSimpleHorizontal
import com.app.zuludin.buqu.core.icons.PhosphorPencil

@Composable
fun NoteActionDialog(
    onDismiss: () -> Unit,
    popupPosition: Offset,
    noteWidth: Int,
    cameraZoom: Float
) {
    Popup(
        popupPositionProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val y = popupPosition.y - 16
                val x =
                    anchorBounds.left + popupPosition.x + ((noteWidth * cameraZoom) - popupContentSize.width) / 2
                return IntOffset(
                    x.toInt(), y.toInt()
                )
            }
        },
        onDismissRequest = { onDismiss() },
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
                            onClick = { onDismiss() },
                        )
                        OverflowMenuItem(
                            title = "Connect",
                            icon = PhosphorLinkSimpleHorizontal,
                            onClick = { onDismiss() },
                        )
                    }
                }
            }
        },
    )
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