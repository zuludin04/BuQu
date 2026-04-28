package com.app.zuludin.buqu.ui.book.upsert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpsertBookUiState(
    val bookId: String? = null,
    val title: String = "",
    val author: String = "",
    val cover: String = "",
    val description: String = "",
    val totalPages: Int = 0,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class UpsertBookViewModel @Inject constructor(
    private val bookRepository: IBookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookId: String? = savedStateHandle[BuquDestinationArgs.BOOK_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertBookUiState())
    val uiState: StateFlow<UpsertBookUiState> = _uiState

    init {
        if (bookId != null) {
            loadBook(bookId)
        }
    }

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            bookRepository.getBookById(bookId)?.let { book ->
                _uiState.update {
                    it.copy(
                        bookId = book.bookId,
                        title = book.title,
                        author = book.author,
                        cover = book.cover,
                        description = book.description,
                        totalPages = book.totalPages
                    )
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onAuthorChange(author: String) {
        _uiState.update { it.copy(author = author) }
    }

    fun onCoverChange(cover: String) {
        _uiState.update { it.copy(cover = cover) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onTotalPagesChange(pages: String) {
        val p = pages.toIntOrNull() ?: 0
        _uiState.update { it.copy(totalPages = p) }
    }

    fun saveBook() {
        viewModelScope.launch {
            bookRepository.upsertBook(
                bookId = _uiState.value.bookId,
                title = _uiState.value.title,
                author = _uiState.value.author,
                cover = _uiState.value.cover,
                description = _uiState.value.description,
                totalPages = _uiState.value.totalPages
            )
            _uiState.update { it.copy(isSuccess = true) }
        }
    }
}
