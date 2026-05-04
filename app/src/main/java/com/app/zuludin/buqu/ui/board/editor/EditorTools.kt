package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorBookOpen
import com.app.zuludin.buqu.core.icons.PhosphorLightbulb
import com.app.zuludin.buqu.core.icons.PhosphorMinus
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import kotlin.math.roundToInt

@Composable
fun BoardTools(
    modifier: Modifier = Modifier,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onResetZoom: () -> Unit,
    scale: Float,
    onImportQuotes: () -> Unit,
    onImportBooks: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 3.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            ButtonTool(imageVector = PhosphorMinus, onClick = { onZoomIn() })
            Text(
                text = "${(scale * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .width(40.dp)
                    .clickable { onResetZoom() },
                textAlign = TextAlign.Center
            )
            ButtonTool(imageVector = PhosphorPlus, onClick = { onZoomOut() })

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
            )

            ButtonTool(
                imageVector = PhosphorLightbulb,
                onClick = { onImportQuotes() }
            )
            ButtonTool(
                imageVector = PhosphorBookOpen,
                onClick = { onImportBooks() }
            )
        }
    }
}

@Composable
private fun ButtonTool(imageVector: ImageVector, onClick: () -> Unit, isActive: Boolean = false) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}