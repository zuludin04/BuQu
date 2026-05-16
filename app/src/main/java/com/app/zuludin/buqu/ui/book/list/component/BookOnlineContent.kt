package com.app.zuludin.buqu.ui.book.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.ui.book.list.BookOnlineState

@Composable
fun BookOnlineContent(
    modifier: Modifier,
    bookOnline: BookOnlineState,
    onBookClick: (Book) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (bookOnline.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Searching online…", style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else if (bookOnline.books.isEmpty()) {
            item {
                OnlineEmptyState(
                    query = "",
                    onSearch = { },
                )
            }
        } else {
            item {
                Text(
                    text = "Online results",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(bookOnline.books) { book ->
                BookItem(
                    book = book,
                    onClick = { onBookClick(book) },
                )
            }
        }
    }
}

@Composable
private fun OnlineEmptyState(
    query: String,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (query.isBlank()) "Type a query, then submit to search online." else "No online results for “$query”.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onSearch, enabled = query.isNotBlank()) {
            Text("Search online")
        }
    }
}