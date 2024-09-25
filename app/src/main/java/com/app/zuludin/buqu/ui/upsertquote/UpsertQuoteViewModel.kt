package com.app.zuludin.buqu.ui.upsertquote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isQuoteSaved: Boolean = false,
    val isError: Boolean = false,
)

@HiltViewModel
class UpsertQuoteViewModel @Inject constructor(
    private val upsertQuoteUseCase: UpsertQuoteUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val quoteId: String? = savedStateHandle[BuquDestinationArgs.QUOTE_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertQuoteUiState())
    val uiState: StateFlow<UpsertQuoteUiState> = _uiState

    init {
        Log.d("QUOTE_ID", quoteId ?: "empty")
    }

    fun saveQuote() = viewModelScope.launch {
        val state = uiState.value
        if (state.quote.isNotEmpty() &&
            state.book.isNotEmpty() &&
            state.author.isNotEmpty() &&
            state.page.isNotEmpty()
        ) {
            upsertQuoteUseCase.invoke(
                quote = state.quote,
                book = state.book,
                author = state.author,
                page = state.page,
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

    fun errorMessageShown() {
        _uiState.update {
            it.copy(isError = false)
        }
    }

    fun onPopScreen() {
        _uiState.update {
            it.copy(
                quote = "",
                book = "",
                page = "",
                author = "",
                isQuoteSaved = false,
                isError = false,
            )
        }
    }
}