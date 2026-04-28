package com.app.zuludin.buqu.ui.book.scan

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

data class CoverScanUiState(
    val ocrText: String = "",
    val query: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<Book> = emptyList(),
    val selectedBook: Book? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
)

@HiltViewModel
class CoverScanViewModel @Inject constructor(
    private val bookRepository: IBookRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoverScanUiState())
    val uiState: StateFlow<CoverScanUiState> = _uiState

    fun onOcrTextSelected(text: String) {
        val query = normalizeOcrToQuery(text)
        _uiState.update {
            it.copy(
                ocrText = text,
                query = query,
                isSearching = query.isNotBlank(),
                searchResults = emptyList(),
                selectedBook = null,
                saveSuccess = false,
            )
        }
        if (query.isBlank()) return

        viewModelScope.launch {
            val results = bookRepository.searchBooks(query)
            _uiState.update { it.copy(searchResults = results, isSearching = false) }
        }
    }

    fun dismissResults() {
        _uiState.update { it.copy(searchResults = emptyList(), isSearching = false, selectedBook = null) }
    }

    fun selectBook(book: Book) {
        _uiState.update { it.copy(selectedBook = book) }
    }

    fun clearSelectedBook() {
        _uiState.update { it.copy(selectedBook = null) }
    }

    fun saveSelectedBook() {
        val book = _uiState.value.selectedBook ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            bookRepository.upsertBook(
                bookId = book.bookId,
                title = book.title,
                author = book.author,
                cover = book.cover,
                description = book.description,
                totalPages = book.totalPages
            )
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}

private fun normalizeOcrToQuery(text: String): String {
    val normalized = text
        .replace("\n", " ")
        .replace("\t", " ")
        .trim()
        .replace(Regex("\\s+"), " ")
    return normalized.take(80)
}

