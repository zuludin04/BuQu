package com.app.zuludin.buqu.ui.quote.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.ui.category.CategoryItem
import androidx.core.graphics.toColorInt

@Composable
fun QuoteFilterSheet(
    categories: List<Category>,
    authors: List<String>,
    books: List<String>,
    onSelectCategory: (Category) -> Unit,
    onSelectAuthor: (String) -> Unit,
    onSelectBook: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(items = categories, itemContent = { item ->
            CategoryItem(
                color = Color("#${item.color}".toColorInt()),
                name = item.name,
                onClick = { onSelectCategory(item) }
            )
        })

        if (authors.isNotEmpty()) {
            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "Authors",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(authors) { author ->
                Text(
                    text = author,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAuthor(author) }
                        .padding(vertical = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (books.isNotEmpty()) {
            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "Books",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(books) { book ->
                Text(
                    text = book,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectBook(book) }
                        .padding(vertical = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        
        item { 
            Spacer(modifier = Modifier.height(32.dp))
        }
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
    QuoteFilterSheet(
        categories = categories,
        authors = emptyList(),
        books = emptyList(),
        onSelectCategory = {},
        onSelectAuthor = {},
        onSelectBook = {}
    )
}