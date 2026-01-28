package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.fontSelector
import com.app.zuludin.buqu.core.theme.provider

@Composable
fun QuoteFontSelector(modifier: Modifier = Modifier, onChangeFont: (FontFamily) -> Unit) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.font_type),
            style = MaterialTheme.typography.caption,
            color = color
        )
        SelectFontType(
            modifier = Modifier.padding(top = 8.dp),
            onSelectFont = onChangeFont,
            textColor = color
        )
    }
}

@Composable
private fun SelectFontType(
    modifier: Modifier = Modifier,
    onSelectFont: (FontFamily) -> Unit,
    textColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    var fontName by remember { mutableStateOf("Rubik") }

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, SolidColor(Color.Gray)),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = fontName,
                color = textColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            fontSelector.forEach { fontType ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        fontName = fontType
                        val font = GoogleFont(fontType)
                        val family = FontFamily(Font(googleFont = font, fontProvider = provider))
                        onSelectFont(family)
                    }
                ) {
                    val font = GoogleFont(fontType)
                    val family = FontFamily(Font(googleFont = font, fontProvider = provider))
                    val selectedColor =
                        if (fontName == fontType) MaterialTheme.colors.secondary else textColor
                    Text(fontType, fontFamily = family, color = selectedColor)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SelectFontTypePreview() {
    SelectFontType(textColor = Color.Gray, onSelectFont = {})
}