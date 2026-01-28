package com.app.zuludin.buqu.ui.quote

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import kotlinx.coroutines.launch

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

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = stringResource(R.string.app_name),
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
            HomeContent(
                quotes = uiState.quotes,
                selectedCategory = uiState.selectedCategory,
                onQuoteClick = onQuoteClick,
                onSelectCategory = { viewModel.filterQuotes(it) },
                isLoading = uiState.isLoading,
                onRefresh = { viewModel.filterQuotes(null) }
            )

            if (showFilterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showFilterSheet = false },
                    sheetState = sheetState
                ) {
                    QuoteFilterSheet(uiState.categories) {
                        showFilterSheet = false
                        viewModel.filterQuotes(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    quotes: List<Quote>,
    selectedCategory: Category?,
    onSelectCategory: (Category?) -> Unit,
    onQuoteClick: (String) -> Unit,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    Column(modifier = modifier) {
        if (selectedCategory != null) {
            QuoteCategoryChips(
                backgroundColor = selectedCategory.color,
                name = selectedCategory.name,
                onClearChip = { onSelectCategory(null) }
            )
        }

        if (quotes.isEmpty()) {
            TasksEmptyContent()
        } else {
            val refreshScope = rememberCoroutineScope()
            val state = rememberPullRefreshState(
                refreshing = isLoading,
                onRefresh = {
                    refreshScope.launch { onRefresh() }
                }
            )

            Box(
                modifier = modifier.pullRefresh(state),
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
                                QuoteItem(
                                    modifier = Modifier.testTag("QuoteItem"),
                                    quote = quotes[it].quote,
                                    backgroundColor = "#${quotes[it].color}",
                                    book = quotes[it].book,
                                    author = quotes[it].author,
                                ) {
                                    onQuoteClick(quotes[it].quoteId)
                                }
                            }
                        }
                    }

                    PullRefreshIndicator(isLoading, state, Modifier.align(Alignment.TopCenter))
                },
            )
        }
    }
}

@Composable
fun TasksEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            stringResource(R.string.empty_quote_message),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}