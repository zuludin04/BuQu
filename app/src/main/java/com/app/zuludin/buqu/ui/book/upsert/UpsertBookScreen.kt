package com.app.zuludin.buqu.ui.book.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.TextScanner
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorMagnifyingGlass

@Composable
fun UpsertBookScreen(
    onBack: () -> Unit,
    onSearchWeb: () -> Unit,
    topAppBarTitle: String,
    savedStateHandle: SavedStateHandle? = null,
    viewModel: UpsertBookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getStateFlow<String?>("selected_book", null)
            ?.collect { book ->
                if (book != null) {
                    viewModel.loadBook(book)
                    savedStateHandle["selected_book"] = null
                }
            }
    }

    Scaffold(
        topBar = {
            BuQuToolbar(
                title = topAppBarTitle,
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(PhosphorArrowLeft, null)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveBook() }) {
                        Icon(PhosphorCheck, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = onSearchWeb,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(PhosphorMagnifyingGlass, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search Web")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.weight(1f)
                )
                TextScanner(onTextSelected = viewModel::onTitleChange)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = uiState.author,
                    onValueChange = viewModel::onAuthorChange,
                    label = { Text("Author") },
                    modifier = Modifier.weight(1f)
                )
                TextScanner(onTextSelected = viewModel::onAuthorChange)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.totalPages.let { if (it == 0) "" else it.toString() },
                onValueChange = viewModel::onTotalPagesChange,
                label = { Text("Total Pages") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.cover,
                onValueChange = viewModel::onCoverChange,
                label = { Text("Cover Image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBack()
        }
    }
}
