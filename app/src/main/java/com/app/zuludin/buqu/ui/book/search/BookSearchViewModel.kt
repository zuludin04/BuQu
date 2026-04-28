package com.app.zuludin.buqu.ui.book.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookSearchUiState(
    val query: String = "",
    val searchResults: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedBook: Book? = null
)

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val bookRepository: IBookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookSearchUiState())
    val uiState: StateFlow<BookSearchUiState> = _uiState

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun searchBooks() {
        if (_uiState.value.query.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val results = bookRepository.searchBooks(_uiState.value.query)
            _uiState.update { it.copy(searchResults = results, isLoading = false) }
        }
    }

    fun selectBook(book: Book) {
        _uiState.update { it.copy(selectedBook = book) }
    }
}
