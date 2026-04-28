package com.app.zuludin.buqu.ui.book.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: IBookRepository
) : ViewModel() {

    val uiState: StateFlow<BookUiState> = bookRepository.observeBooks()
        .map { BookUiState(books = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookUiState(isLoading = true)
        )

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            bookRepository.deleteBook(bookId)
        }
    }
}
