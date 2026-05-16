package com.app.zuludin.buqu.ui.quote.list

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.icons.PhosphorLightbulb
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onQuoteClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                BuQuToolbar(title = stringResource(R.string.app_name))

                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.searchQuotes(it) },
                    onSearch = { viewModel.searchQuotes(it) },
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text("Search quotes, authors, or books") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    windowInsets = WindowInsets(top = 16.dp),
                ) {}
            }
        },
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            quotes = uiState.quotes,
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onQuoteClick = onQuoteClick,
            onSelectCategory = { viewModel.filterQuotes(it) },
            showCategoryFilter = uiState.showCategoryFilter,
            isLoading = uiState.isLoading
        )
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    quotes: List<Quote>,
    categories: List<Category>,
    selectedCategory: Category?,
    onSelectCategory: (Category?) -> Unit,
    onQuoteClick: (String) -> Unit,
    showCategoryFilter: Boolean,
    isLoading: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showCategoryFilter) {
            LazyRow(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(categories.size) {
                    QuoteFilterChip(
                        text = categories[it].name,
                        isSelected = selectedCategory == categories[it],
                        onClick = {
                            if (selectedCategory == categories[it]) {
                                onSelectCategory(null)
                            } else {
                                onSelectCategory(categories[it])
                            }
                        }
                    )
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (quotes.isEmpty()) {
                TasksEmptyContent(icon = PhosphorLightbulb, message = R.string.empty_quote_message)
            } else {
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
                            imagePath = quotes[it].image,
                            category = quotes[it].category
                        ) {
                            onQuoteClick(quotes[it].quoteId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TasksEmptyContent(modifier: Modifier = Modifier, icon: ImageVector, @StringRes message: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            stringResource(message),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}