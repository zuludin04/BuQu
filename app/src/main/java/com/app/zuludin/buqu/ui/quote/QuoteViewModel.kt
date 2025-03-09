package com.app.zuludin.buqu.ui.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.Async
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuoteUiState(
    val quotes: List<Quote> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    quoteRepository: QuoteRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _categoryId = MutableStateFlow("")

    private val _q = combine(quoteRepository.getQuotes(), _categoryId) { quotes, categoryId ->
        filterQuote(quotes, categoryId)
    }
        .map { Async.Success(it) }
        .catch<Async<List<Quote>>> { emit(Async.Error("Error while load quotes")) }

    private val _categories = categoryRepository.getCategories()

    val uiState: StateFlow<QuoteUiState> =
        combine(
            _isLoading,
            _userMessage,
            _q,
            _categories
        ) { isLoading, userMessage, quotes, categories ->
            when (quotes) {
                Async.Loading -> {
                    QuoteUiState(isLoading = true)
                }

                is Async.Error -> {
                    QuoteUiState(isLoading = false, userMessage = quotes.errorMessage)
                }

                is Async.Success -> {
                    val cats = mutableListOf<Category>()
                    viewModelScope.launch {
                        cats.addAll(categories)
                    }
                    QuoteUiState(
                        quotes = quotes.data,
                        isLoading = isLoading,
                        userMessage = userMessage,
                        categories = cats
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = QuoteUiState(isLoading = true)
        )

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun setFiltering(categoryId: String) {
        _categoryId.value = categoryId
    }

    private fun filterQuote(quotes: List<Quote>, categoryId: String): List<Quote> {
        val quotesToShow = ArrayList<Quote>()

        if (categoryId == "") {
            quotesToShow.addAll(quotes)
        } else {
            val filter = quotes.filter { it.categoryId == categoryId }
            quotesToShow.addAll(filter)
        }

        return quotesToShow
    }
}