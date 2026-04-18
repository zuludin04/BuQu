package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorLineSegments
import com.app.zuludin.buqu.core.icons.PhosphorMinus
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.core.icons.PhosphorSelectionAll
import kotlin.math.roundToInt

@Composable
fun ZoomPanTool(
    modifier: Modifier = Modifier,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onResetZoom: () -> Unit,
    scale: Float
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        tonalElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            IconButton(onClick = { onZoomIn() }) {
                Icon(PhosphorMinus, null, modifier = Modifier.size(18.dp))
            }
            Text(
                text = "${(scale * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .width(40.dp)
                    .clickable { onResetZoom() },
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onZoomOut() }) {
                Icon(PhosphorPlus, null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SelectConnectTool(
    modifier: Modifier = Modifier,
    isSelectionMode: Boolean,
    isConnectionMode: Boolean,
    onToggleSelectionMode: () -> Unit,
    onToggleConnectionMode: () -> Unit,
) {
    Row(modifier = modifier) {
        IconButton(
            modifier = Modifier.background(
                color = if (isConnectionMode) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.9f
                ) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
            ),
            onClick = { onToggleConnectionMode() },
            content = {
                Icon(
                    imageVector = PhosphorLineSegments,
                    contentDescription = null,
                    tint = if (isConnectionMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        IconButton(
            modifier = Modifier.background(
                color = if (isSelectionMode) MaterialTheme.colorScheme.tertiary.copy(
                    alpha = 0.9f
                ) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ), onClick = { onToggleSelectionMode() }, content = {
                Icon(
                    imageVector = PhosphorSelectionAll,
                    contentDescription = null,
                    tint = if (isSelectionMode) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

@Composable
fun SelectionConnectionIndicator(modifier: Modifier, isConnectionMode: Boolean) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = if (isConnectionMode) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp
        ) {
            Text(
                text = if (isConnectionMode) "Connection Mode" else "Selection Mode",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isConnectionMode) "Tap two cards to link them"
                else "Tap cards to select/deselect",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}