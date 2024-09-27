package com.app.zuludin.buqu.ui.upsertquote

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.util.SpeechRecognizerContract

@Composable
fun UpsertQuoteScreen(
    topAppBarTitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpsertQuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {
            val result = it.toString()
            viewModel.updateQuote(result.substring(1, result.length - 1))
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(scaffoldState = scaffoldState, topBar = {
        Toolbar(title = topAppBarTitle, onBack = {
            onBack()
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        })
    }, bottomBar = {
        BottomAppBar(actions = {
            if (topAppBarTitle == "Update Quote") {
                IconButton(onClick = viewModel::deleteQuote) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Localized description"
                    )
                }
            }
            IconButton(onClick = {
                speechRecognizerLauncher.launch(Unit)
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_mic),
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = "Localized description",
                )
            }
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::saveQuote,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check), "Localized description"
                )
            }
        })
    }) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            val focusManager = LocalFocusManager.current

            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = "Quote",
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
                    label = "Book",
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
                    label = "Page",
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
                label = "Author",
                capitalization = KeyboardCapitalization.Words,
                value = uiState.author,
                onChanged = viewModel::updateAuthor,
                imeAction = ImeAction.Done,
                keyboardAction = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Toolbar(title: String, onBack: () -> Unit) {
    TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
        Text(title)
    }, navigationIcon = {
        FilledTonalIconButton(
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            onClick = onBack,
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
            )
        }
    })
}

@Composable
private fun TitleInputField(
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    value: String,
    onChanged: (String) -> Unit,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier.padding(vertical = 4.dp),
        value = value,
        singleLine = singleLine,
        label = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        onValueChange = { v ->
            if (v.isNotEmpty()) {
                onChanged(v)
            }
        },
        placeholder = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardAction,
    )
}