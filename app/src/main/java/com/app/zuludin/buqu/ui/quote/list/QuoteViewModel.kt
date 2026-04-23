package com.app.zuludin.buqu.ui.quote.list

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
import javax.inject.Inject

data class QuoteUiState(
    val quotes: List<Quote> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedAuthor: String? = null,
    val selectedBook: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    quoteRepository: QuoteRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _selectedAuthor = MutableStateFlow<String?>(null)
    private val _selectedBook = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")

    private val baseState: Flow<QuoteUiState> = combine(
        quoteRepository.observeQuotes(),
        categoryRepository.observeCategories()
    ) { quotes, categories ->
        QuoteUiState(quotes = quotes, categories = categories, isLoading = false)
    }
        .onStart { emit(QuoteUiState(isLoading = true)) }
        .catch { e -> emit(QuoteUiState(userMessage = e.message, isLoading = false)) }

    val uiState: StateFlow<QuoteUiState> =
        combine(
            baseState,
            _selectedCategory,
            _selectedAuthor,
            _selectedBook,
            _searchQuery
        ) { state, cat, author, book, query ->
            val filteredQuotes = filterQuote(state.quotes, cat, author, book, query)
            state.copy(
                quotes = filteredQuotes,
                selectedCategory = cat,
                selectedAuthor = author,
                selectedBook = book,
                searchQuery = query,
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

    fun filterByAuthor(author: String?) {
        _selectedAuthor.value = author
    }

    fun filterByBook(book: String?) {
        _selectedBook.value = book
    }

    fun searchQuotes(query: String) {
        _searchQuery.value = query
    }

    private fun filterQuote(
        quotes: List<Quote>,
        category: Category?,
        author: String?,
        book: String?,
        query: String
    ): List<Quote> {
        return quotes.filter { quote ->
            val matchesCategory = category == null || quote.categoryId == category.categoryId
            val matchesAuthor = author == null || quote.author == author
            val matchesBook = book == null || quote.book == book
            val matchesQuery = query.isEmpty() ||
                    quote.quote.contains(query, ignoreCase = true) ||
                    quote.author.contains(query, ignoreCase = true) ||
                    quote.book.contains(query, ignoreCase = true)

            matchesCategory && matchesAuthor && matchesBook && matchesQuery
        }
    }
}
