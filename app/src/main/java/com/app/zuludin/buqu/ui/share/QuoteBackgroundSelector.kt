package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.gradientBackgrounds

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuoteBackgroundSelector(
    modifier: Modifier = Modifier,
    onChangeGradient: (List<Color>) -> Unit
) {
    var selectedIndexBackground by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        val color = if (isSystemInDarkTheme()) Color.White else Color.Black
        Text(text = stringResource(R.string.background), style = MaterialTheme.typography.caption, color = color)
        FlowRow(modifier = Modifier.padding(top = 8.dp)) {
            repeat(gradientBackgrounds.size) {
                BackgroundGradientItem(
                    colors = gradientBackgrounds[it],
                    isSelected = it == selectedIndexBackground,
                    onSelected = {
                        selectedIndexBackground = it
                        onChangeGradient(gradientBackgrounds[it])
                    },
                )
            }
        }
    }
}

@Composable
private fun BackgroundGradientItem(
    colors: List<Color>,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val selectedBorder =
        if (isSelected) MaterialTheme.colors.onBackground else Color.Transparent
    Box(
        modifier = Modifier
            .padding(1.dp)
            .size(40.dp)
            .background(
                brush = Brush.linearGradient(colors = colors),
                shape = RoundedCornerShape(5.dp)
            )
            .border(width = 1.dp, color = selectedBorder, shape = RoundedCornerShape(5.dp))
            .clickable { onSelected() }
    )
}

@Preview
@Composable
private fun BackgroundGradientItemPreview() {
    BackgroundGradientItem(gradientBackgrounds.first(), true) {}
}

@Preview
@Composable
private fun QuoteBackgroundSelectorPreview() {
    QuoteBackgroundSelector {}
}