package com.app.zuludin.buqu.ui.quote.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuoteFilterChip(
    text: String,
    onClear: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Surface(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        shadowElevation = 3.dp,
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = contentColor)
            IconButton(onClick = onClear) {
                Icon(Icons.Filled.Clear, null, tint = contentColor)
            }
        }
    }
}

@Preview
@Composable
private fun QuoteFilterChipPreview() {
    QuoteFilterChip(
        text = "Motivation",
        onClear = {}
    )
}
