package com.app.zuludin.buqu.ui.quote

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
import com.app.zuludin.buqu.domain.models.Category

@Composable
fun QuoteCategoryChips(category: Category, onClearChip: () -> Unit) {
    Surface(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        shadowElevation = 3.dp,
        color = Color(android.graphics.Color.parseColor("#${category.color}")),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(category.name, color = Color.White)
            IconButton(onClick = onClearChip) {
                Icon(Icons.Filled.Clear, null, tint = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun QuoteCategoryChipsPreview() {
    val cat = Category(
        categoryId = "",
        name = "Motivation",
        color = "E91E63",
        type = ""
    )
    QuoteCategoryChips(cat) {}
}