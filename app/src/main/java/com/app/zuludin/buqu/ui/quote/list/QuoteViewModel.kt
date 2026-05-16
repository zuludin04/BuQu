package com.app.zuludin.buqu.ui.quote.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    quoteRepository: QuoteRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<QuoteState> =
        combine(
            quoteRepository.observeQuotes(),
            categoryRepository.observeCategories(),
            _selectedCategory,
            _searchQuery
        ) { quotes, categories, cat, query ->
            val filteredQuotes = filterQuote(quotes, cat, query)
            QuoteState(
                quotes = filteredQuotes,
                categories = categories,
                isLoading = false,
                showCategoryFilter = quotes.isNotEmpty()
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = QuoteState(isLoading = true)
            )

    fun filterQuotes(category: Category?) {
        _selectedCategory.value = category
    }

    fun searchQuotes(query: String) {
        _searchQuery.value = query
    }

    private fun filterQuote(
        quotes: List<Quote>,
        category: Category?,
        query: String
    ): List<Quote> {
        return quotes.filter { quote ->
            val matchesCategory = category == null || quote.categoryId == category.categoryId
            val matchesQuery = query.isEmpty() ||
                    quote.quote.contains(query, ignoreCase = true) ||
                    quote.author.contains(query, ignoreCase = true) ||
                    quote.book.contains(query, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }
}
