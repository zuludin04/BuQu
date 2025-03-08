package com.app.zuludin.buqu.ui.quote

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.ui.category.CategoryItem

@Composable
fun QuoteFilterSheet(categories: List<Category>, onSelectCategory: (Category) -> Unit) {
    LazyColumn {
        items(items = categories, itemContent = { item ->
            CategoryItem(
                color = Color(android.graphics.Color.parseColor("#${item.color}")),
                category = item,
                onClick = { onSelectCategory(item) }
            )
        })
    }
}

@Preview
@Composable
private fun QuoteFilterSheetPreview() {
    val categories = listOf(
        Category(
            categoryId = "",
            name = "Motivation",
            color = "E91E63",
            type = ""
        ),
        Category(
            categoryId = "",
            name = "Motivation",
            color = "E91E63",
            type = ""
        )
    )
    QuoteFilterSheet(categories) {}
}