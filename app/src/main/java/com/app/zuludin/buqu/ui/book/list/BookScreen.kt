package com.app.zuludin.buqu.ui.book.list

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.TextSelectionDialog
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorPencil
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import com.app.zuludin.buqu.domain.models.Book
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    onBookClick: (String) -> Unit,
    onAddOnlineBookClick: (String) -> Unit,
    onAddManualBookClick: () -> Unit,
    viewModel: BookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showImageScanner by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val file = remember { context.createImageFile() }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                showImageScanner = true
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            Column {
                BuQuToolbar(title = stringResource(R.string.app_name))

                SearchBar(
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = {
                        if (uiState.scope == BookSearchScope.Online) {
                            viewModel.searchOnline()
                        }
                    },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text("Search book title") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.query.isNotBlank()) {
                            IconButton(
                                onClick = { viewModel.clearQuery() },
                                content = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear search query"
                                    )
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {}

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.scope == BookSearchScope.Saved,
                        onClick = { viewModel.setScope(BookSearchScope.Saved) },
                        label = { Text("Saved") }
                    )
                    FilterChip(
                        selected = uiState.scope == BookSearchScope.Online,
                        onClick = { viewModel.setScope(BookSearchScope.Online) },
                        label = { Text("Online") }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (uiState.scope) {
            BookSearchScope.Saved -> {
                val showEmptyBooksState = uiState.books.isEmpty() && uiState.query.isBlank()
                if (showEmptyBooksState) {
                    EmptyBooksState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        onManualInput = { onAddManualBookClick() },
                        onScanClick = {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.CAMERA
                            )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    )
                    return@Scaffold
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.savedResults.isEmpty() && uiState.query.isNotBlank()) {
                        item {
                            SearchOnlineCtaRow(
                                query = uiState.query.trim(),
                                onSearchOnline = {
                                    viewModel.setScope(BookSearchScope.Online)
                                    viewModel.searchOnline()
                                }
                            )
                        }
                    }

                    items(uiState.savedResults) { book ->
                        BookItem(book = book, onClick = { onBookClick(book.bookId) })
                    }
                }
            }

            BookSearchScope.Online -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.isOnlineLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Searching online…",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else if (uiState.onlineErrorMessage != null) {
                        item {
                            OnlineErrorState(
                                message = uiState.onlineErrorMessage ?: "",
                                onRetry = { viewModel.searchOnline() }
                            )
                        }
                    } else if (uiState.onlineResults.isEmpty()) {
                        item {
                            OnlineEmptyState(
                                query = uiState.query.trim(),
                                onSearch = { viewModel.searchOnline() }
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
                        items(uiState.onlineResults) { book ->
                            BookItem(book = book, onClick = { onAddOnlineBookClick(book.bookId) })
                        }
                    }
                }
            }
        }
    }

    if (showImageScanner) {
        TextSelectionDialog(
            bitmap = context.fixImageRotation(uri!!)!!,
            onDismiss = { showImageScanner = !showImageScanner },
            onTextSelected = { selectedText ->
                viewModel.onQueryChange(selectedText)
                viewModel.setScope(BookSearchScope.Online)
                viewModel.searchOnline()
                showImageScanner = !showImageScanner
            }
        )
    }
}

@Composable
private fun EmptyBooksState(
    modifier: Modifier = Modifier,
    onManualInput: () -> Unit,
    onScanClick: () -> Unit
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
    modifier: Modifier = Modifier
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

@Composable
private fun OnlineEmptyState(
    query: String,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
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

@Composable
private fun OnlineErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp),
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
                modifier = Modifier.size(60.dp, 90.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (book.totalPages > 0) {
                    Text(
                        text = "${book.totalPages} pages",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
