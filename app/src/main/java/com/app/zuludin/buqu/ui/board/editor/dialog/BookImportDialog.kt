package com.app.zuludin.buqu.ui.board.editor.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.icons.PhosphorBookOpen
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorMagnifyingGlass
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.ui.quote.list.TasksEmptyContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookImportDialog(
    onDismiss: () -> Unit,
    onImportBooks: (List<Book>) -> Unit,
    books: List<Book>
) {
    var searchQuery by remember { mutableStateOf("") }
    var mutableBooks by remember { mutableStateOf(books) }

    val filteredBooks = mutableBooks.filter { book ->
        book.title.contains(searchQuery, ignoreCase = true) ||
                book.author.contains(searchQuery, ignoreCase = true)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    Column {
                        BuQuToolbar(
                            title = "Import Books",
                            backButton = {
                                IconButton(onClick = onDismiss) {
                                    Icon(PhosphorX, null)
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    val results = mutableBooks.filter { it.isSelected }
                                    onImportBooks(results)
                                }) {
                                    Icon(PhosphorCheck, null)
                                }
                            }
                        )

                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { searchQuery = it },
                            active = false,
                            onActiveChange = {},
                            placeholder = { Text("Search books") },
                            leadingIcon = { Icon(PhosphorMagnifyingGlass, null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {}
                    }
                }
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    if (filteredBooks.isEmpty()) {
                        TasksEmptyContent(
                            icon = PhosphorBookOpen,
                            message = R.string.book_empty
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredBooks) { book ->
                                BookImportItem(
                                    book = book,
                                    onClick = {
                                        val selection = mutableBooks.map {
                                            if (it.bookId == book.bookId) {
                                                val isSelected = it.isSelected
                                                it.copy(isSelected = !isSelected)
                                            } else {
                                                it
                                            }
                                        }
                                        mutableBooks = selection
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookImportItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = if (book.isSelected) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.cover,
                contentDescription = null,
                modifier = Modifier.size(50.dp, 75.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (book.isSelected) {
                Icon(
                    PhosphorCheck,
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
