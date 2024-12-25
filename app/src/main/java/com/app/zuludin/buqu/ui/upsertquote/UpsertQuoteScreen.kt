package com.app.zuludin.buqu.ui.upsertquote

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.ColorSpinner
import com.app.zuludin.buqu.core.compose.TitleInputField
import com.app.zuludin.buqu.core.utils.SpeechRecognizerContract
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.domain.models.Quote
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Objects

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
    val context = LocalContext.current

    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = SpeechRecognizerContract(), onResult = {
            val result = it.toString()
            viewModel.updateQuote(result.substring(1, result.length - 1))
        })

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )

    val recognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        capturedImageUri = uri
        val bitmap = MediaStore.Images.Media.getBitmap(
            context.contentResolver, capturedImageUri
        )

        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image).addOnSuccessListener { visionText ->
            viewModel.updateQuote(visionText.text)
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

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(scaffoldState = scaffoldState, topBar = {
        BuQuToolbar(
            title = topAppBarTitle,
            backButton = {
                IconButton(onClick = {
                    onBack()
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }, bottomBar = {
        BottomAppBar(actions = {
            if (topAppBarTitle == "Update Quote") {
                IconButton(onClick = viewModel::deleteQuote) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Localized description"
                    )
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
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
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

            IconButton(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_image),
                    contentDescription = "Localized description"
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
            if (uiState.categories.isNotEmpty()) {
                ColorSpinner(
                    currentCategory = if (uiState.category == null) uiState.categories[0] else uiState.category!!,
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