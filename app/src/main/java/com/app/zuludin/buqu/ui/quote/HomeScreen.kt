package com.app.zuludin.buqu.ui.quote

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.util.BuQuToolbar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onQuoteClick: (String) -> Unit,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = { BuQuToolbar("BuQu") },
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            QuotesContent(
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
private fun QuotesContent(
    loading: Boolean,
    quotes: List<Quote>,
    onQuoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (quotes.isEmpty() && !loading) {
        TasksEmptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = {},
            modifier = modifier,
            content = {
                Column(modifier = modifier.fillMaxSize()) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalItemSpacing = 16.dp
                    ) {
                        items(quotes.size) {
                            QuoteItem(quote = quotes[it]) {
                                onQuoteClick(quotes[it].quoteId)
                            }
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun TasksEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )
        Text("Save the Greatest Quote")
    }
}