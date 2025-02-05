package com.app.zuludin.buqu.ui.quote

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onQuoteClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember {
        mutableStateOf(
            Category(
                categoryId = "",
                name = "",
                color = "",
                type = ""
            )
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = {
            BuQuToolbar(
                title = "BuQu",
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Filled.List, null)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (selectedCategoryFilter.categoryId != "") {
                QuoteCategoryChips(category = selectedCategoryFilter) {
                    viewModel.setFiltering("")
                    selectedCategoryFilter = Category(
                        categoryId = "",
                        name = "",
                        color = "",
                        type = ""
                    )
                }
            }

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

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState
            ) {
                QuoteFilterSheet(uiState.categories) {
                    showFilterSheet = false
                    selectedCategoryFilter = it
                    viewModel.setFiltering(it.categoryId)
                }
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