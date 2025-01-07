package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuoteTextPosition(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Position", style = MaterialTheme.typography.caption)
        TextPositionContainer(title = "Quote")
        TextPositionContainer(title = "Book")
        TextPositionContainer(title = "Author")
    }
}

@Composable
private fun TextPositionContainer(title: String) {
    var selectedItem by remember { mutableIntStateOf(0) }

    Column {
        Text(text = title, modifier = Modifier.padding(top = 8.dp))
        Row(modifier = Modifier.padding(top = 4.dp)) {
            SelectableOptionItem(
                modifier = Modifier.weight(1f),
                label = "Start",
                isSelected = selectedItem == 0,
                onSelect = { selectedItem = 0 },
            )
            SelectableOptionItem(
                modifier = Modifier.weight(1f), label = "Center",
                isSelected = selectedItem == 1,
                onSelect = { selectedItem = 1 },
            )
            SelectableOptionItem(
                modifier = Modifier.weight(1f), label = "End",
                isSelected = selectedItem == 2,
                onSelect = { selectedItem = 2 },
            )
        }
    }
}

@Composable
private fun SelectableOptionItem(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val selectedBorder = if (isSelected) MaterialTheme.colors.onPrimary else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(width = 1.dp, color = selectedBorder, shape = CircleShape)
                .padding(4.dp)
                .background(MaterialTheme.colors.primary, shape = CircleShape)
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview
@Composable
private fun SelectableOptionItemPreview() {
    SelectableOptionItem(label = "Start", isSelected = true) {}
}

@Preview
@Composable
private fun TextPositionContainerPreview() {
    TextPositionContainer("Quote")
}

@Preview
@Composable
private fun QuoteTextPositionPreview() {
    QuoteTextPosition()
}