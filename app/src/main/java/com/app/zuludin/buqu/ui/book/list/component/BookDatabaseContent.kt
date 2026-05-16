package com.app.zuludin.buqu.ui.book.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorPencil
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.ui.book.list.BookAction
import com.app.zuludin.buqu.ui.book.list.BookState

@Composable
fun BookDatabaseContent(
    modifier: Modifier,
    uiState: BookState,
    onAction: (BookAction) -> Unit,
    onBookClick: (Book) -> Unit,
    onScanClick: () -> Unit,
    onAddManualBookClick: () -> Unit
) {
    val showEmptyBooksState =
        uiState.bookDatabase.books.isEmpty() && uiState.query.isBlank()
    if (uiState.bookDatabase.isLoading) {
        Box(
            modifier = modifier
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (showEmptyBooksState) {
        EmptyBooksState(
            modifier = modifier
                .padding(16.dp),
            onManualInput = { onAddManualBookClick() },
            onScanClick = { onScanClick() },
        )
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.bookDatabase.books.isEmpty() && uiState.query.isNotBlank()) {
                item {
                    SearchOnlineCtaRow(
                        query = uiState.query.trim(),
                        onSearchOnline = { onAction(BookAction.BookSearchCta("")) },
                    )
                }
            }
            items(uiState.bookDatabase.books) { book ->
                BookItem(
                    book = book,
                    onClick = { onBookClick(book) },
                )
            }
        }
    }
}

@Composable
private fun EmptyBooksState(
    modifier: Modifier = Modifier,
    onManualInput: () -> Unit,
    onScanClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your books will appear here",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Browse your saved list, add books by searching, or scan a cover photo.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onManualInput) {
                Icon(PhosphorPencil, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Manual Input")
            }
            Button(onClick = onScanClick) {
                Icon(PhosphorAperture, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan")
            }
        }
    }
}

@Composable
private fun SearchOnlineCtaRow(
    query: String,
    onSearchOnline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "No matches in Saved.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = onSearchOnline) {
                Text("Search online for \"$query\"")
            }
        }
    }
}