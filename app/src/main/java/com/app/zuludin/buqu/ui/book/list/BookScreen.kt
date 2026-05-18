package com.app.zuludin.buqu.ui.book.list

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.TextSelectionDialog
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import com.app.zuludin.buqu.ui.book.list.component.BookDatabaseContent
import com.app.zuludin.buqu.ui.book.list.component.BookOnlineContent
import kotlinx.coroutines.flow.collectLatest
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    onBookClick: (String, String) -> Unit,
    onAddManualBookClick: () -> Unit,
    viewModel: BookViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
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

    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is BookEvent.ErrorOnline -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                BuQuToolbar(title = stringResource(R.string.app_name))

                SearchBar(
                    query = uiState.query,
                    onQueryChange = { viewModel.onAction(BookAction.SearchBooks(it)) },
                    onSearch = { viewModel.onAction(BookAction.SearchBooks(it)) },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text("Search book title") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.query.isNotBlank()) {
                            IconButton(
                                onClick = { viewModel.onAction(BookAction.ClearQuery) },
                                content = {
                                    Icon(
                                        imageVector = PhosphorX,
                                        contentDescription = "Clear search query"
                                    )
                                },
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    windowInsets = WindowInsets(top = 16.dp),
                    content = {},
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.scope == BookSearchScope.Saved,
                        onClick = { viewModel.onAction(BookAction.ChangeScope(BookSearchScope.Saved)) },
                        label = { Text("Saved") },
                    )
                    FilterChip(
                        selected = uiState.scope == BookSearchScope.Online,
                        onClick = { viewModel.onAction(BookAction.ChangeScope(BookSearchScope.Online)) },
                        label = { Text("Online") },
                    )
                }
            }
        },
    ) { paddingValues ->
        when (uiState.scope) {
            BookSearchScope.Saved -> {
                BookDatabaseContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    uiState = uiState,
                    onAction = viewModel::onAction,
                    onBookClick = { onBookClick(it.title, it.bookId) },
                    onScanClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    onAddManualBookClick = { onAddManualBookClick() }
                )
            }

            BookSearchScope.Online -> {
                BookOnlineContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    bookOnline = uiState.bookOnline,
                    onBookClick = { onBookClick(it.title, it.bookId) }
                )
            }
        }
    }

    if (showImageScanner) {
        TextSelectionDialog(
            bitmap = context.fixImageRotation(uri!!)!!,
            onDismiss = { showImageScanner = !showImageScanner },
            onTextSelected = { selectedText ->
                viewModel.onAction(BookAction.ChangeScope(BookSearchScope.Online))
                viewModel.onAction(BookAction.SearchBooks(selectedText))
                showImageScanner = !showImageScanner
            },
        )
    }
}
