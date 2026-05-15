package com.app.zuludin.buqu.ui.quote.upsert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BookSpinner
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.ColorSpinner
import com.app.zuludin.buqu.core.compose.MediaFileScanner
import com.app.zuludin.buqu.core.compose.SpeechToText
import com.app.zuludin.buqu.core.compose.TextSelectionDialog
import com.app.zuludin.buqu.core.compose.TitleInputField
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorShareNetwork
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.theme.provider
import com.app.zuludin.buqu.core.utils.convertPathFileToUri
import com.app.zuludin.buqu.core.utils.fixImageRotation
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun UpsertQuoteScreen(
    topAppBarTitle: String,
    onBack: () -> Unit,
    onShareQuote: (quote: String, author: String, book: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpsertQuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is UpsertQuoteEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }

                UpsertQuoteEvent.GoHome -> onBack()
            }
        }
    }

    UpsertQuoteContent(
        topAppBarTitle = topAppBarTitle,
        onBack = onBack,
        scaffoldState = scaffoldState,
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction,
        onShareQuote = onShareQuote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpsertQuoteContent(
    topAppBarTitle: String,
    onBack: () -> Unit,
    onShareQuote: (quote: String, author: String, book: String) -> Unit,
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    uiState: UpsertQuoteState,
    onAction: (UpsertQuoteAction) -> Unit
) {
    val context = LocalContext.current

    var showPreviewImage by remember { mutableStateOf(false) }
    var showTextSelection by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = topAppBarTitle,
                backButton = {
                    IconButton(onClick = {
                        onBack()
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }) {
                        Icon(PhosphorArrowLeft, null)
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    if (topAppBarTitle == "Update Quote") {
                        IconButton(
                            onClick = { onAction(UpsertQuoteAction.DeleteQuote) },
                            content = {
                                Icon(
                                    imageVector = PhosphorTrash,
                                    contentDescription = "Localized description"
                                )
                            },
                        )

                        IconButton(onClick = {
                            val field = uiState.field
                            val book = uiState.books.first { it.bookId == field.bookId }
                            onShareQuote(field.quote, book.author, book.title)
                        }) {
                            Icon(PhosphorShareNetwork, "Localized description")
                        }
                    }

                    SpeechToText { onAction(UpsertQuoteAction.UpdateQuote(it)) }

                    MediaFileScanner(
                        imageVector = PhosphorAperture,
                        isOpenCamera = true,
                        onTextSelected = { onAction(UpsertQuoteAction.UpdateQuote(it)) },
                        onSaveImage = { path, _ ->
                            onAction(UpsertQuoteAction.PickImage(path))
                        }
                    )

                    MediaFileScanner(
                        imageVector = PhosphorImage,
                        isOpenCamera = false,
                        onTextSelected = { onAction(UpsertQuoteAction.UpdateQuote(it)) },
                        onSaveImage = { path, _ ->
                            onAction(UpsertQuoteAction.PickImage(path))
                        }
                    )
                }, floatingActionButton = {
                    FloatingActionButton(
                        modifier = Modifier.testTag("AddNewQuote"),
                        onClick = { onAction(UpsertQuoteAction.SaveQuote) },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        content = {
                            Icon(PhosphorCheck, "Localized description")
                        }
                    )
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                FilterChip(
                    selected = !uiState.isSavingAsImage,
                    onClick = { onAction(UpsertQuoteAction.ToggleSavingMode(false)) },
                    label = { Text("Text") },
                    enabled = uiState.field.quote.isNotEmpty(),
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = uiState.isSavingAsImage,
                    onClick = { onAction(UpsertQuoteAction.ToggleSavingMode(true)) },
                    label = { Text("Image") },
                    enabled = uiState.field.image.isNotEmpty()
                )
            }

            if (uiState.isSavingAsImage && uiState.field.image.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .aspectRatio(16f / 9f),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 2.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(File(uiState.field.image))
                                .crossfade(true)
                                .build(),
                            contentDescription = "Quote Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { showPreviewImage = true },
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = { onAction(UpsertQuoteAction.DeleteQuote) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = PhosphorX,
                                contentDescription = "Remove Image",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (!uiState.isSavingAsImage) {
                TitleInputField(
                    modifier = Modifier.fillMaxWidth().testTag("QuoteField"),
                    singleLine = false,
                    label = stringResource(R.string.quote),
                    capitalization = KeyboardCapitalization.Sentences,
                    value = uiState.field.quote,
                    onChanged = { onAction(UpsertQuoteAction.UpdateQuote(it)) },
                    imeAction = ImeAction.Done,
                    keyboardAction = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    minLines = 5,
                    textStyle = TextStyle(
                        fontStyle = FontStyle.Italic, fontFamily = FontFamily(
                            Font(
                                googleFont = GoogleFont("Playfair Display"),
                                fontProvider = provider
                            )
                        )
                    )
                )
            }

            BookSpinner(
                modifier = Modifier.padding(top = 12.dp).testTag("BookField"),
                currentBookId = uiState.field.bookId,
                books = uiState.books,
                onSelectBook = { onAction(UpsertQuoteAction.SelectBook(it)) }
            )

            ColorSpinner(
                modifier = Modifier.padding(top = 12.dp).testTag("CategoryField"),
                currentCategoryId = uiState.field.categoryId,
                categories = uiState.categories,
                onSelectCategory = { onAction(UpsertQuoteAction.SelectCategory(it)) }
            )
        }
    }

    if (showTextSelection) {
        TextSelectionDialog(
            bitmap = context.fixImageRotation(uiState.field.image.convertPathFileToUri())!!,
            onDismiss = { showTextSelection = !showTextSelection },
            onTextSelected = { selectedText ->
                onAction(UpsertQuoteAction.ScanTextFromImage(selectedText))
                showTextSelection = !showTextSelection
            }
        )
    }

    if (showPreviewImage) {
        ScanImageTextSheet(
            imagePath = uiState.field.image,
            onDismiss = { showPreviewImage = !showPreviewImage },
            onScanText = {
                showTextSelection = true
                showPreviewImage = !showPreviewImage
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanImageTextSheet(onDismiss: () -> Unit, imagePath: String, onScanText: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(imagePath))
                    .crossfade(true)
                    .build(),
                contentDescription = "Quote Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onScanText() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    "Scan Text",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
