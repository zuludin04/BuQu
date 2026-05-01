package com.app.zuludin.buqu.ui.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDetailUiState(
    val book: Book? = null,
    val quotes: List<Quote> = emptyList(),
    val fromDatabase: Boolean = true,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isDeleted: Boolean = false
)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: IBookRepository,
    private val quoteRepository: IQuoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookId: String? = savedStateHandle[BuquDestinationArgs.BOOK_ID_ARG]

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState

    init {
        if (bookId != null) {
            loadBook(bookId)
            observeQuotes(bookId)
        }
    }

    fun loadBook(bookId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            bookRepository.getBookById(bookId)?.let { book ->
                _uiState.update {
                    it.copy(
                        book = book,
                        isLoading = false,
                        fromDatabase = book.fromDatabase
                    )
                }
            }
        }
    }

    private fun observeQuotes(bookId: String) {
        viewModelScope.launch {
            quoteRepository.observeQuotes().collectLatest { quotes ->
                val filtered = quotes.filter { it.bookId == bookId }
                _uiState.update { it.copy(quotes = filtered) }
            }
        }
    }

    fun saveBook() {
        viewModelScope.launch {
            bookRepository.upsertBook(
                bookId = _uiState.value.book?.bookId,
                title = _uiState.value.book?.title ?: "",
                author = _uiState.value.book?.author ?: "",
                cover = _uiState.value.book?.cover ?: "",
                description = _uiState.value.book?.description ?: "",
                totalPages = _uiState.value.book?.totalPages ?: 0,
                publisher = _uiState.value.book?.publisher ?: "",
                year = _uiState.value.book?.year ?: 0
            )
            _uiState.update { it.copy(isSuccess = true, fromDatabase = true) }
        }
    }

    fun deleteBook() {
        viewModelScope.launch {
            bookRepository.deleteBook(_uiState.value.book!!.bookId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}