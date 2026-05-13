package com.app.zuludin.buqu.ui.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.usecase.book.GetBookDetailUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: IBookRepository,
    private val getBookDetail: GetBookDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookId: String? = savedStateHandle[BuquDestinationArgs.BOOK_ID_ARG]

    private val _uiState = MutableStateFlow(BookDetailState())
    val uiState: StateFlow<BookDetailState> = _uiState

    private val _eventChannel = Channel<BookDetailEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        if (bookId != null) {
            loadData(bookId)
        }
    }

    fun saveBook() {
//        viewModelScope.launch {
//            bookRepository.upsertBook(
//                bookId = _uiState.value.book?.bookId,
//                title = _uiState.value.book?.title ?: "",
//                author = _uiState.value.book?.author ?: "",
//                cover = _uiState.value.book?.cover ?: "",
//                description = _uiState.value.book?.description ?: "",
//                totalPages = _uiState.value.book?.totalPages ?: 0,
//                publisher = _uiState.value.book?.publisher ?: "",
//                year = _uiState.value.book?.year ?: 0
//            )
//            _uiState.update { it.copy(isSuccess = true, fromDatabase = true) }
//        }
    }

    fun deleteBook() {
        viewModelScope.launch {
            bookRepository.deleteBook(_uiState.value.book!!.bookId)
            _eventChannel.send(BookDetailEvent.GoHome)
        }
    }

    private fun loadData(bookId: String) {
        viewModelScope.launch {
            getBookDetail.invoke(bookId).let { data ->
                _uiState.update {
                    it.copy(
                        book = data.book,
                        quotes = data.quotes,
                        isLoading = false,
                        fromDatabase = data.book?.fromDatabase ?: true
                    )
                }
            }
        }
    }
}