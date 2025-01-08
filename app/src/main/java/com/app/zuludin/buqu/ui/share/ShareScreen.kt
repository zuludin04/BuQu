package com.app.zuludin.buqu.ui.share

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.gradientBackgrounds
import com.app.zuludin.buqu.core.theme.BuQuTheme
import com.app.zuludin.buqu.core.theme.bodyFontFamily
import com.app.zuludin.buqu.core.utils.shareImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShareScreen(
    book: String,
    quote: String,
    author: String,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val writeStorageAccessState = rememberMultiplePermissionsState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            emptyList()
        } else {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    )

    fun shareQuote() {
        if (writeStorageAccessState.allPermissionsGranted) {
            coroutineScope.launch {
                val bitmap = graphicsLayer.toImageBitmap()
                context.shareImage(
                    "Share Image",
                    bitmap.asAndroidBitmap(),
                    "screenshot-${System.currentTimeMillis()}.png"
                )
            }
        } else if (writeStorageAccessState.shouldShowRationale) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "The storage permission is needed to save the image",
                    actionLabel = "Grant Access"
                )

                if (result == SnackbarResult.ActionPerformed) {
                    writeStorageAccessState.launchMultiplePermissionRequest()
                }
            }
        } else {
            writeStorageAccessState.launchMultiplePermissionRequest()
        }
    }

    var quoteShareBackground by remember { mutableStateOf(gradientBackgrounds[0]) }
    var quoteBookVisibility by remember { mutableStateOf(true) }
    var quoteAuthorVisibility by remember { mutableStateOf(true) }
    var bookPosition by remember { mutableStateOf(Alignment.CenterHorizontally) }
    var authorPosition by remember { mutableStateOf(Alignment.CenterHorizontally) }
    var fontFamily by remember { mutableStateOf(bodyFontFamily) }

    Scaffold(
        topBar = {
            BuQuToolbar(
                title = "Share",
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { shareQuote() },
                content = { Icon(painterResource(R.drawable.ic_share), null) }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                QuoteShareContainer(
                    book,
                    quote,
                    author,
                    backgroundColors = quoteShareBackground,
                    visibleAuthor = quoteAuthorVisibility,
                    visibleBook = quoteBookVisibility,
                    bookPosition = bookPosition,
                    authorPosition = authorPosition,
                    fontFamily = fontFamily
                )
            }
            QuoteShareEditor(
                onChangeGradient = { quoteShareBackground = it },
                onAuthorVisibility = { quoteAuthorVisibility = it },
                onBookVisibility = { quoteBookVisibility = it },
                onBookPosition = { bookPosition = it },
                onAuthorPosition = { authorPosition = it },
                onChangeFont = { fontFamily = it }
            )
        }
    }
}

@Composable
private fun QuoteShareContainer(
    book: String,
    quote: String,
    author: String,
    backgroundColors: List<Color> = listOf(),
    visibleBook: Boolean = true,
    visibleAuthor: Boolean = true,
    bookPosition: Alignment.Horizontal = Alignment.CenterHorizontally,
    authorPosition: Alignment.Horizontal = Alignment.CenterHorizontally,
    fontFamily: FontFamily = bodyFontFamily
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Brush.linearGradient(colors = backgroundColors))
    ) {
        AnimatedVisibility(visible = visibleBook, modifier = Modifier.align(bookPosition)) {
            Text(text = book, fontSize = 18.sp, fontFamily = fontFamily)
        }
        Text(
            text = quote,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontFamily = fontFamily,
            textAlign = TextAlign.Center
        )
        AnimatedVisibility(visible = visibleAuthor, modifier = Modifier.align(authorPosition)) {
            Text(text = "— $author —", fontFamily = fontFamily)
        }
    }
}

@Composable
private fun QuoteShareEditor(
    onChangeGradient: (List<Color>) -> Unit,
    onAuthorVisibility: (Boolean) -> Unit,
    onBookVisibility: (Boolean) -> Unit,
    onAuthorPosition: (Alignment.Horizontal) -> Unit,
    onBookPosition: (Alignment.Horizontal) -> Unit,
    onChangeFont: (FontFamily) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        QuoteBackgroundSelector(onChangeGradient = onChangeGradient)
        QuoteInfoVisibility(
            modifier = Modifier.padding(top = 16.dp),
            onAuthorVisibility = onAuthorVisibility,
            onBookVisibility = onBookVisibility
        )
        QuoteFontSelector(
            onChangeFont = onChangeFont
        )
        QuoteTextPosition(
            modifier = Modifier.padding(top = 16.dp),
            onAuthorPosition = onAuthorPosition,
            onBookPosition = onBookPosition
        )
    }
}

@Preview
@Composable
private fun QuoteShareContainerPreview() {
    QuoteShareContainer(
        author = "ASA",
        quote = "Asa saasa saa sa a",
        book = "Asa Asa",
    )
}

@Preview
@Composable
private fun QuoteShareEditorPreview() {
    BuQuTheme {
        Surface {
            QuoteShareEditor(
                onChangeGradient = {},
                onBookVisibility = {},
                onAuthorVisibility = {},
                onBookPosition = {},
                onAuthorPosition = {},
                onChangeFont = {}
            )
        }
    }
}