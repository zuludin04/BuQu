package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.R

@Composable
fun QuoteTextPosition(
    modifier: Modifier = Modifier,
    onBookPosition: (Alignment.Horizontal) -> Unit,
    onAuthorPosition: (Alignment.Horizontal) -> Unit,
) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.position),
            style = MaterialTheme.typography.caption,
            color = color
        )
        TextPositionContainer(
            title = stringResource(R.string.book),
            onSelect = onBookPosition,
            textColor = color
        )
        TextPositionContainer(
            title = stringResource(R.string.author),
            onSelect = onAuthorPosition,
            textColor = color
        )
    }
}

@Composable
private fun TextPositionContainer(
    title: String,
    onSelect: (Alignment.Horizontal) -> Unit,
    textColor: Color
) {
    var selectedItem by remember { mutableIntStateOf(1) }

    fun generatePositionAlignment(index: Int): Alignment.Horizontal {
        return when (index) {
            0 -> Alignment.Start
            1 -> Alignment.CenterHorizontally
            2 -> Alignment.End
            else -> Alignment.CenterHorizontally
        }
    }

    Column {
        Text(text = title, modifier = Modifier.padding(top = 8.dp), color = textColor)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            SelectableOptionItem(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.start),
                isSelected = selectedItem == 0,
                textColor = textColor,
                onSelect = {
                    selectedItem = 0
                    onSelect(generatePositionAlignment(selectedItem))
                },
            )
            SelectableOptionItem(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.center),
                isSelected = selectedItem == 1,
                textColor = textColor,
                onSelect = {
                    selectedItem = 1
                    onSelect(generatePositionAlignment(selectedItem))
                },
            )
            SelectableOptionItem(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.end),
                isSelected = selectedItem == 2,
                textColor = textColor,
                onSelect = {
                    selectedItem = 2
                    onSelect(generatePositionAlignment(selectedItem))
                },
            )
        }
    }
}

@Composable
private fun SelectableOptionItem(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    textColor: Color
) {
    val selectedBorder = if (isSelected) textColor else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onSelect() }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(width = 1.dp, color = selectedBorder, shape = CircleShape)
                .padding(4.dp)
                .background(MaterialTheme.colors.secondary, shape = CircleShape)
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 4.dp),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun SelectableOptionItemPreview() {
    SelectableOptionItem(label = "Start", isSelected = true, textColor = Color.White, onSelect = {})
}

@Preview
@Composable
private fun TextPositionContainerPreview() {
    TextPositionContainer("Quote", textColor = Color.White, onSelect = {})
}

@Preview
@Composable
private fun QuoteTextPositionPreview() {
    QuoteTextPosition(onAuthorPosition = {}, onBookPosition = {})
}