package com.app.zuludin.buqu.ui.addquote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.usecases.UpsertQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddQuoteUiState(
    val quote: String = "",
    val book: String = "",
    val page: String = "",
    val author: String = "",
    val isQuoteSaved: Boolean = false
)

@HiltViewModel
class AddQuoteViewModel @Inject constructor(private val upsertQuoteUseCase: UpsertQuoteUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AddQuoteUiState())
    val uiState: StateFlow<AddQuoteUiState> = _uiState

    fun saveQuote() = viewModelScope.launch {
        val state = uiState.value
        upsertQuoteUseCase.invoke(
            quote = state.quote,
            book = state.book,
            author = state.author,
            page = state.page
        )
        _uiState.update {
            it.copy(isQuoteSaved = true)
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
}