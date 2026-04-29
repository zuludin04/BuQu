package com.app.zuludin.buqu.ui.quote.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuoteFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shadowElevation = 3.dp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }
    ) {
        Text(
            text,
            modifier = Modifier.padding(12.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
private fun QuoteFilterChipPreview() {
    QuoteFilterChip(
        text = "Motivation",
        isSelected = false,
        onClick = {},
    )
}
