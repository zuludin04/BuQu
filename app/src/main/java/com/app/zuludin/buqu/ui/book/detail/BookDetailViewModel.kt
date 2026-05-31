package com.app.zuludin.buqu.ui.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.usecase.book.GetBookDetailUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: IBookRepository,
    getBookDetail: GetBookDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val bookId: String = checkNotNull(savedStateHandle[BuquDestinationArgs.BOOK_ID_ARG])

    val uiState = getBookDetail.invoke(bookId).stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = BookDetailState(isLoading = true)
    )

    private val _eventChannel = Channel<BookDetailEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun saveBook() {
        viewModelScope.launch {
            bookRepository.upsertBook(
                bookId = uiState.value.book?.bookId,
                title = uiState.value.book?.title ?: "",
                author = uiState.value.book?.author ?: "",
                cover = uiState.value.book?.cover ?: "",
                description = uiState.value.book?.description ?: "",
                totalPages = uiState.value.book?.totalPages ?: 0,
                publisher = uiState.value.book?.publisher ?: "",
                year = uiState.value.book?.year ?: 0,
            )
        }
    }

    fun deleteBook() {
        viewModelScope.launch {
            bookRepository.deleteBook(uiState.value.book!!.bookId)
            _eventChannel.send(BookDetailEvent.GoHome)
        }
    }
}