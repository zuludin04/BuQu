package com.app.zuludin.buqu.ui.upsertquote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.usecases.DeleteQuoteUseCase
import com.app.zuludin.buqu.domain.usecases.GetQuoteDetailUseCase
import com.app.zuludin.buqu.domain.usecases.UpsertQuoteUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpsertQuoteUiState(
    val quote: String = "",
    val book: String = "",
    val page: String = "",
    val author: String = "",
    val categoryId: String = "",
    val isQuoteSaved: Boolean = false,
    val isError: Boolean = false,
)

@HiltViewModel
class UpsertQuoteViewModel @Inject constructor(
    private val upsertQuoteUseCase: UpsertQuoteUseCase,
    private val getQuoteDetailUseCase: GetQuoteDetailUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val quoteId: String? = savedStateHandle[BuquDestinationArgs.QUOTE_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertQuoteUiState())
    val uiState: StateFlow<UpsertQuoteUiState> = _uiState

    init {
        if (quoteId != null) {
            loadQuote(quoteId)
        }
    }

    fun saveQuote() = viewModelScope.launch {
        val state = uiState.value
        if (state.quote.isNotEmpty() &&
            state.book.isNotEmpty() &&
            state.author.isNotEmpty() &&
            state.page.isNotEmpty()
        ) {
            upsertQuoteUseCase.invoke(
                quoteId = quoteId,
                quote = state.quote,
                book = state.book,
                author = state.author,
                page = state.page,
                categoryId = state.categoryId
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
            deleteQuoteUseCase.invoke(quoteId)
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

    fun updateBook(newBook: String) {
        _uiState.update {
            it.copy(book = newBook)
        }
    }

    fun updatePage(newPage: String) {
        _uiState.update {
            it.copy(page = newPage)
        }
    }

    fun updateAuthor(newAuthor: String) {
        _uiState.update {
            it.copy(author = newAuthor)
        }
    }

    fun updateImage(newImage: String) {
        _uiState.update {
            it.copy(categoryId = newImage)
        }
    }

    fun errorMessageShown() {
        _uiState.update {
            it.copy(isError = false)
        }
    }

    private fun loadQuote(quoteId: String) {
        viewModelScope.launch {
            getQuoteDetailUseCase.invoke(quoteId).let { quote ->
                if (quote != null) {
                    _uiState.update {
                        it.copy(
                            quote = quote.quote,
                            book = quote.book,
                            page = quote.page.toString(),
                            author = quote.author,
                            categoryId = quote.categoryId
                        )
                    }
                }
            }

        }
    }
}