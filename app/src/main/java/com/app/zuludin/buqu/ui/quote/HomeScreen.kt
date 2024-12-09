package com.app.zuludin.buqu.ui.quote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.util.BuQuToolbar
import com.app.zuludin.buqu.util.LoadingContent
import kotlin.random.Random

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onQuoteClick: (String) -> Unit,
    onAddQuote: () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = { BuQuToolbar("BuQu") },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddQuote) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HotContent(
                loading = uiState.isLoading,
                quotes = uiState.quotes,
                onQuoteClick = onQuoteClick
            )
        }

        uiState.userMessage?.let { message ->
            LaunchedEffect(scaffoldState, viewModel, message) {
                scaffoldState.snackbarHostState.showSnackbar(message)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
private fun HotContent(
    loading: Boolean,
    quotes: List<Quote>,
    onQuoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        LoadingContent(loading = loading, empty = quotes.isEmpty() && !loading, emptyContent = {}) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {
                    items(quotes) {
                        HotTake(hotTake = it) {
                            onQuoteClick(it.quoteId)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotTake(hotTake: Quote, onClick: () -> Unit) {
    val color = Color(Random.nextLong(0xffffffff)).copy(alpha = 0.1f)
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hotTake.book,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = hotTake.quote,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = hotTake.author,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}