package com.app.zuludin.buqu.ui.quote.upsert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.data.repositories.BookRepository
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpsertQuoteUiState(
    val quote: String = "",
    val book: String = "",
    val bookId: String? = null,
    val page: String = "",
    val author: String = "",
    val image: String = "",
    val isSavingAsImage: Boolean = false,
    val category: Category = Category(
        categoryId = "a76c5015-34c7-4a54-bdfb-c5ed2010b7c9",
        name = "Motivation",
        color = "03A9F4",
        type = "Quote"
    ),
    val categories: List<Category> = emptyList(),
    val books: List<Book> = emptyList(),
    val isQuoteSaved: Boolean = false,
    val isError: Boolean = false,
)

@HiltViewModel
class UpsertQuoteViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val categoryRepository: CategoryRepository,
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val quoteId: String? = savedStateHandle[BuquDestinationArgs.QUOTE_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertQuoteUiState())
    val uiState: StateFlow<UpsertQuoteUiState> = _uiState

    init {
        loadCategories()
        loadBooks()
        if (quoteId != null) {
            loadQuote(quoteId)
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            bookRepository.observeBooks().collect { books ->
                _uiState.update { it.copy(books = books) }
            }
        }
    }

    fun selectBook(book: Book?) {
        _uiState.update {
            it.copy(
                book = book?.title ?: "",
                bookId = book?.bookId,
                author = book?.author ?: ""
            )
        }
    }

    fun saveQuote() = viewModelScope.launch {
        val state = uiState.value
        val quoteToSave = if (state.isSavingAsImage) "" else state.quote
        val imageToSave = if (state.isSavingAsImage) state.image else ""

        if (quoteToSave.isNotEmpty() || imageToSave.isNotEmpty()) {
            quoteRepository.upsertQuote(
                quoteId = quoteId,
                quote = quoteToSave,
                book = state.book,
                author = state.author,
                page = if (state.page.isEmpty()) 0 else state.page.toInt(),
                categoryId = state.category.categoryId,
                image = imageToSave,
                bookId = state.bookId
            )
            _uiState.update {
                it.copy(isQuoteSaved = true, isError = false)
            }
        } else {
            _uiState.update {
                it.copy(isError = true)
            }
        }
    }

    fun deleteQuote() = viewModelScope.launch {
        if (quoteId != null) {
            quoteRepository.deleteQuote(quoteId)
            _uiState.update {
                it.copy(isQuoteSaved = true)
            }
        }
    }

    fun updateQuote(newQuote: String) {
        _uiState.update {
            it.copy(quote = newQuote)
        }
    }

    fun updateImage(newImage: String) {
        _uiState.update {
            it.copy(image = newImage, isSavingAsImage = true)
        }
    }

    fun removeImage() {
        _uiState.update {
            it.copy(image = "", isSavingAsImage = false)
        }
    }

    fun removeQuoteText() {
        _uiState.update {
            it.copy(quote = "", isSavingAsImage = true)
        }
    }

    fun updateSavingMode(isImage: Boolean) {
        _uiState.update {
            it.copy(isSavingAsImage = isImage)
        }
    }

    fun updateCategory(category: Category) {
        _uiState.update {
            it.copy(
                category = category
            )
        }
    }

    fun errorMessageShown() {
        _uiState.update {
            it.copy(isError = false)
        }
    }

    fun loadQuote(quoteId: String) {
        viewModelScope.launch {
            quoteRepository.getQuoteById(quoteId).let { quote ->
                if (quote != null) {
                    _uiState.update {
                        it.copy(
                            quote = quote.quote,
                            book = quote.book,
                            page = quote.page.toString(),
                            author = quote.author,
                            image = quote.image,
                            bookId = quote.bookId,
                            isSavingAsImage = quote.image.isNotBlank(),
                            category = Category(
                                categoryId = quote.categoryId,
                                name = quote.category,
                                color = quote.color,
                                type = "Quote"
                            )
                        )
                    }
                }
            }

        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.observeCategories().first().let { categories ->
                _uiState.update {
                    it.copy(categories = categories)
                }
            }
        }
    }
}