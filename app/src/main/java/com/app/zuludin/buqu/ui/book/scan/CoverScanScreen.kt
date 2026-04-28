package com.app.zuludin.buqu.ui.book.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.TextScanner
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.ui.book.list.BookItem

@Composable
fun CoverScanScreen(
    onBack: () -> Unit,
    onEditBook: (String) -> Unit,
    onSaved: () -> Unit,
    viewModel: CoverScanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onSaved()
    }

    Scaffold(
        topBar = {
            BuQuToolbar(
                title = "Scan cover",
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(PhosphorArrowLeft, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Take a photo of the cover and select the title text. We’ll search for matching books so you can save it.",
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextScanner(
                    imageVector = PhosphorAperture,
                    onTextSelected = viewModel::onOcrTextSelected
                )
                Spacer(Modifier.height(0.dp))
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = if (uiState.query.isBlank()) "Open camera" else "Last: ${uiState.query}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (uiState.isSearching) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (uiState.searchResults.isNotEmpty()) {
        CoverScanMatchesDialog(
            results = uiState.searchResults,
            onDismiss = viewModel::dismissResults,
            onSelect = viewModel::selectBook
        )
    }

    if (uiState.selectedBook != null) {
        CoverScanConfirmDialog(
            book = uiState.selectedBook!!,
            isSaving = uiState.isSaving,
            onDismiss = viewModel::clearSelectedBook,
            onSave = viewModel::saveSelectedBook,
            onEdit = { onEditBook(uiState.selectedBook!!.bookId) }
        )
    }
}

@Composable
private fun CoverScanMatchesDialog(
    results: List<Book>,
    onDismiss: () -> Unit,
    onSelect: (Book) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Matches") },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results.take(8)) { book ->
                    BookItem(book = book, onClick = { onSelect(book) })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
    )
}

@Composable
private fun CoverScanConfirmDialog(
    book: Book,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onEdit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(book.title.ifBlank { "Book details" }) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (book.cover.isNotBlank()) {
                    AsyncImage(
                        model = book.cover,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                }
                Text(text = book.author.ifBlank { "Unknown author" })
                if (book.totalPages > 0) Text(text = "${book.totalPages} pages")
                if (book.description.isNotBlank()) {
                    Text(
                        text = book.description,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (isSaving) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = onEdit, enabled = !isSaving) {
                    Text("Edit")
                }
                TextButton(onClick = onSave, enabled = !isSaving) {
                    Text("Save")
                }
            }
        }
    )
}

