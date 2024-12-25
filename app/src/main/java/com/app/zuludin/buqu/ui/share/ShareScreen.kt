package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.compose.BuQuToolbar

@Composable
fun ShareScreen(
    book: String,
    quote: String,
    author: String,
    onBack: () -> Unit,
) {
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
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            QuoteShareContainer(book, quote, author)
        }
    }
}

@Composable
private fun QuoteShareContainer(book: String, quote: String, author: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Brush.linearGradient(colors = listOf(Color.Blue, Color.Gray)))
    ) {
        Text(text = book)
        Text(text = quote)
        Text(text = "— $author —")
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