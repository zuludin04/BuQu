package com.app.zuludin.buqu.ui.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class BookSearchScope {
    Saved,
    Online
}

data class BookUiState(
    val books: List<Book> = emptyList(),
    val query: String = "",
    val scope: BookSearchScope = BookSearchScope.Saved,
    val savedResults: List<Book> = emptyList(),
    val onlineResults: List<Book> = emptyList(),
    val isOnlineLoading: Boolean = false,
    val onlineErrorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: IBookRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val scope = MutableStateFlow(BookSearchScope.Saved)

    private val onlineResults = MutableStateFlow<List<Book>>(emptyList())
    private val isOnlineLoading = MutableStateFlow(false)
    private val onlineErrorMessage = MutableStateFlow<String?>(null)

    private data class OnlineState(
        val results: List<Book>,
        val isLoading: Boolean,
        val errorMessage: String?
    )

    private val onlineState = combine(
        onlineResults,
        isOnlineLoading,
        onlineErrorMessage
    ) { results, isLoading, errorMessage ->
        OnlineState(results = results, isLoading = isLoading, errorMessage = errorMessage)
    }

    val uiState: StateFlow<BookUiState> = combine(
        bookRepository.observeBooks(),
        query,
        scope,
        onlineState
    ) { books, query, scope, online ->
        val trimmedQuery = query.trim()
        val savedResults = if (trimmedQuery.isBlank()) {
            books
        } else {
            val q = trimmedQuery.lowercase()
            books.filter { book ->
                book.title.lowercase().contains(q) || book.author.lowercase().contains(q)
            }
        }

        BookUiState(
            books = books,
            query = query,
            scope = scope,
            savedResults = savedResults,
            onlineResults = online.results,
            isOnlineLoading = online.isLoading,
            onlineErrorMessage = online.errorMessage,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = BookUiState(isLoading = true)
    )

    fun onQueryChange(newQuery: String) {
        query.value = newQuery
    }

    fun clearQuery() {
        query.value = ""
        onlineErrorMessage.value = null
    }

    fun setScope(newScope: BookSearchScope) {
        scope.value = newScope
    }

    fun searchOnline() {
        val q = query.value.trim()
        if (q.isBlank()) return

        viewModelScope.launch {
            isOnlineLoading.value = true
            onlineErrorMessage.value = null
            try {
                onlineResults.value = bookRepository.searchBooks(q)
            } catch (t: Throwable) {
                onlineErrorMessage.value = t.message ?: "Failed to search online."
                onlineResults.value = emptyList()
            } finally {
                isOnlineLoading.value = false
            }
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            bookRepository.deleteBook(bookId)
        }
    }
}
