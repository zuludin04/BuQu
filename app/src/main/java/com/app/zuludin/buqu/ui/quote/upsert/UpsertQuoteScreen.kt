package com.app.zuludin.buqu.ui.quote.upsert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.ColorSpinner
import com.app.zuludin.buqu.core.compose.MediaFileScanner
import com.app.zuludin.buqu.core.compose.SpeechToText
import com.app.zuludin.buqu.core.compose.TitleInputField
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorShareNetwork
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.domain.models.Quote
import java.io.File

@Suppress("DEPRECATION")
@Composable
fun UpsertQuoteScreen(
    topAppBarTitle: String,
    onBack: () -> Unit,
    onShareQuote: (Quote) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpsertQuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            BottomAppBar(actions = {
                if (topAppBarTitle == "Update Quote") {
                    IconButton(onClick = viewModel::deleteQuote) {
                        Icon(PhosphorTrash, "Localized description")
                    }

                    IconButton(onClick = {
                        val quote = Quote(
                            quoteId = "",
                            quote = uiState.quote,
                            author = uiState.author,
                            book = uiState.book,
                            page = 0,
                            category = "",
                            categoryId = "",
                            color = ""
                        )
                        onShareQuote(quote)
                    }) {
                        Icon(PhosphorShareNetwork, "Localized description")
                    }
                }

                SpeechToText { viewModel.updateQuote(it) }

                MediaFileScanner(
                    imageVector = PhosphorAperture,
                    isOpenCamera = true,
                    onTextSelected = { viewModel.updateQuote(it) },
                    onSaveImage = { path, _ -> viewModel.updateImage(path) }
                )
            }, floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.testTag("AddNewQuote"),
                    onClick = viewModel::saveQuote,
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(PhosphorCheck, "Localized description")
                }
            })
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current

            if (uiState.image.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .aspectRatio(16f / 9f),
                    shape = RoundedCornerShape(8.dp),
                    tonalElevation = 2.dp
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(File(uiState.image))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Quote Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = stringResource(R.string.quote),
                capitalization = KeyboardCapitalization.Sentences,
                value = uiState.quote,
                onChanged = viewModel::updateQuote,
                imeAction = ImeAction.Next,
                keyboardAction = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )
            Row {
                TitleInputField(
                    modifier = Modifier.weight(2f),
                    label = stringResource(R.string.book),
                    capitalization = KeyboardCapitalization.Words,
                    value = uiState.book,
                    onChanged = viewModel::updateBook,
                    imeAction = ImeAction.Next,
                    keyboardAction = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Right)
                    })
                )
                Box(modifier = Modifier.padding(horizontal = 8.dp))
                TitleInputField(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.page),
                    keyboardType = KeyboardType.Number,
                    value = uiState.page,
                    onChanged = viewModel::updatePage,
                    imeAction = ImeAction.Next,
                    keyboardAction = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )
            }
            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.author),
                capitalization = KeyboardCapitalization.Words,
                value = uiState.author,
                onChanged = viewModel::updateAuthor,
                imeAction = ImeAction.Done,
                keyboardAction = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
            if (uiState.categories.isNotEmpty()) {
                ColorSpinner(
                    modifier = Modifier.padding(top = 12.dp),
                    currentCategory = uiState.category,
                    categories = uiState.categories,
                    onSelectCategory = viewModel::updateCategory
                )
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isQuoteSaved) {
            onBack()
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }

        if (uiState.isError) {
            scaffoldState.snackbarHostState.showSnackbar("Make sure to fill all forms")
            viewModel.errorMessageShown()
        }
    }
}
