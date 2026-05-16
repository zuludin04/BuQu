package com.app.zuludin.buqu.ui.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: IBookRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookState())
    val uiState: StateFlow<BookState> = _uiState

    private val _eventChannel = Channel<BookEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            bookRepository.observeBooks().collect { books ->
                _uiState.update {
                    it.copy(
                        bookDatabase = it.bookDatabase.copy(
                            isLoading = false,
                            books = books
                        )
                    )
                }
            }
        }
    }

    fun onAction(action: BookAction) {
        when (action) {
            is BookAction.ChangeScope -> setScope(action.scope)
            is BookAction.SearchBooks -> {
                onQueryChange(action.query)
                if (_uiState.value.scope == BookSearchScope.Online) {
                    searchOnline(action.query)
                } else {
                    searchDatabase(action.query)
                }
            }

            BookAction.ClearQuery -> clearQuery()
            is BookAction.BookSearchCta -> {
                setScope(BookSearchScope.Online)
                onQueryChange(_uiState.value.query)
                searchOnline(_uiState.value.query)
            }
        }
    }

    private fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    private fun clearQuery() {
        _uiState.update { it.copy(query = "") }
        searchDatabase("")
    }

    private fun setScope(newScope: BookSearchScope) {
        _uiState.update { it.copy(scope = newScope) }
    }

    private fun searchDatabase(query: String) {
        viewModelScope.launch {
            val trimmedQuery = query.trim()
            val books = bookRepository.observeBooks().first()
            val searchResults = if (trimmedQuery.isBlank()) {
                books
            } else {
                val q = trimmedQuery.lowercase()
                books.filter { book ->
                    book.title.lowercase().contains(q) || book.author.lowercase().contains(q)
                }
            }
            _uiState.update { it.copy(bookDatabase = it.bookDatabase.copy(books = searchResults)) }
        }
    }

    private fun searchOnline(query: String) {
        val q = query.trim()
        if (q.isBlank()) return
        _uiState.update { it.copy(bookOnline = it.bookOnline.copy(isLoading = true)) }

        viewModelScope.launch {
            try {
                val results = bookRepository.searchBooks(q)
                _uiState.update {
                    it.copy(
                        bookOnline = it.bookOnline.copy(
                            isLoading = false,
                            books = results
                        )
                    )
                }
            } catch (t: Throwable) {
                val message = t.message ?: "Failed to search online."
                _eventChannel.send(BookEvent.ErrorOnline(message))
            }
        }
    }
}
