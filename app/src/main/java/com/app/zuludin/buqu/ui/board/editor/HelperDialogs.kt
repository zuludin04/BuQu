package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.InputHelperChip
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorLinkBreak
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.icons.PhosphorNote
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorXCircle
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.ui.quote.list.TasksEmptyContent

@Composable
fun BoardNameDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    val colors = listOf(
        "E1F5FE", "FFF9C4", "F1F8E9", "FFEBEE", "F3E5F5", "EFEBE9"
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Save Board") }, text = {
        Column {
            Text("Please enter a name and pick a color for this board.")

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Board Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Board Theme", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color("#${color}".toColorInt()))
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color) MaterialTheme.colorScheme.primary
                                else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color })
                }
            }
        }
    }, confirmButton = {
        TextButton(
            onClick = { if (name.isNotBlank()) onConfirm(name, selectedColor) },
            enabled = name.isNotBlank()
        ) {
            Text("Save")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputDialog(
    inputText: String, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit
) {
    var text by remember { mutableStateOf(inputText) }
    val colors = listOf(
        "E1F5FE", "FFF9C4", "F1F8E9", "FFEBEE", "F3E5F5", "EFEBE9"
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Add New Card",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                minLines = 3,
                maxLines = 5,
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "" }) {
                            Icon(PhosphorXCircle, null)
                        }
                    }
                })

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputHelperChip(
                    label = "Voice",
                    icon = PhosphorMicrophone,
                    onClick = { }
                )

                InputHelperChip(
                    label = "Scan",
                    icon = PhosphorAperture,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Color", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color("#${color}".toColorInt()))
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color) MaterialTheme.colorScheme.primary
                                else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text, selectedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = text.isNotBlank()
            ) {
                Text("Create Card", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteConnectDialog(source: NoteCard, notes: List<NoteCard>, onDismiss: (NoteCard?) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val potentialTargets = notes.filter { it.noteId != source.noteId }

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Connect to...",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Linking from: \"${source.title.take(20)}${if (source.title.length > 20) "..." else ""}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (potentialTargets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No other cards to connect to.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(potentialTargets) { target ->
                        val noteColor = Color("#${target.color}".toColorInt())
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(noteColor.copy(alpha = 0.3f))
                                .border(1.dp, noteColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    onDismiss(target)
                                }
                                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(noteColor)
                                    .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = target.title,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = PhosphorLinkBreak,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteImportDialog(
    onDismiss: () -> Unit,
    onQuoteSelected: (Quote) -> Unit,
    onImportQuotes: () -> Unit,
    quotes: List<Quote>,
    categories: List<Category>
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val filteredQuotes = quotes.filter { quote ->
        val matchesSearch = quote.quote.contains(searchQuery, ignoreCase = true) ||
                quote.author.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null || quote.categoryId == selectedCategory
        matchesSearch && matchesCategory
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
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (active) 0.dp else 16.dp),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        placeholder = { Text("Search by quote or author...") },
                        leadingIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(PhosphorArrowLeft, contentDescription = null)
                            }
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(PhosphorTrash, contentDescription = null)
                                }
                            }
                        }
                    ) {}
                },
                bottomBar = {
                    Surface(tonalElevation = 8.dp) {
                        Button(
                            onClick = { onImportQuotes() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Import Selected Quote")
                        }
                    }
                }
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category.categoryId,
                                onClick = {
                                    selectedCategory =
                                        if (selectedCategory == category.categoryId) null else category.categoryId
                                },
                                label = { Text(category.name) },
                                leadingIcon = if (selectedCategory == category.categoryId) {
                                    {
                                        Icon(
                                            PhosphorCheck,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                } else null
                            )
                        }
                    }

                    Divider()

                    if (filteredQuotes.isEmpty()) {
                        TasksEmptyContent(icon = PhosphorNote, message = R.string.note_not_found)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredQuotes) { quote ->
                                QuoteImportItem(
                                    quote = quote,
                                    onClick = { onQuoteSelected(quote) }
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
fun QuoteImportItem(quote: Quote, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = if (quote.isSelected) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (quote.isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"${quote.quote}\"",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${quote.author} - ${quote.book}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = quote.category,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}