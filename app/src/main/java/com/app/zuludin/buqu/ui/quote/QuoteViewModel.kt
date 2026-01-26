package com.app.zuludin.buqu.ui.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuoteUiState(
    val quotes: List<Quote> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)

    private val baseState: Flow<QuoteUiState> = combine(
        quoteRepository.getQuotes(),
        categoryRepository.getCategories()
    ) { quotes, categories ->
        QuoteUiState(quotes = quotes, categories = categories, isLoading = false)
    }
        .onStart { emit(QuoteUiState(isLoading = true)) }
        .catch { e -> emit(QuoteUiState(userMessage = e.message, isLoading = false)) }

    val uiState: StateFlow<QuoteUiState> =
        combine(
            baseState,
            _selectedCategory
        ) { state, cat ->
            val filteredQuotes = filterQuote(state.quotes, cat)
            state.copy(
                quotes = filteredQuotes,
                selectedCategory = cat,
                isLoading = false
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = QuoteUiState(isLoading = true)
            )

    fun filterQuotes(category: Category?) {
        _selectedCategory.value = category
    }

    private fun filterQuote(quotes: List<Quote>, category: Category?): List<Quote> {
        val quotesToShow = ArrayList<Quote>()

        if (category == null) {
            quotesToShow.addAll(quotes)
        } else {
            viewModelScope.launch {
                val filter = quoteRepository.getQuotesByCategory(category.categoryId)
                quotesToShow.addAll(filter)
            }
        }

        return quotesToShow
    }
}