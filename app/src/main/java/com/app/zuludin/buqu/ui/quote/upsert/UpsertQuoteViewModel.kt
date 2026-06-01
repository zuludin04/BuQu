package com.app.zuludin.buqu.ui.quote.upsert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.InvalidQuoteException
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.usecase.quote.GetDetailQuoteUseCase
import com.app.zuludin.buqu.domain.usecase.quote.UpsertQuoteUseCase
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
class UpsertQuoteViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val getDetailQuote: GetDetailQuoteUseCase,
    private val upsertQuote: UpsertQuoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val quoteId: String? = savedStateHandle[BuquDestinationArgs.QUOTE_ID_ARG]
    private val bookId: String? = savedStateHandle[BuquDestinationArgs.BOOK_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertQuoteState())
    val uiState: StateFlow<UpsertQuoteState> = _uiState

    private val _eventChannel = Channel<UpsertQuoteEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    fun onAction(action: UpsertQuoteAction) {
        when (action) {
            UpsertQuoteAction.SaveQuote -> saveQuote()
            UpsertQuoteAction.DeleteQuote -> deleteQuote()
            is UpsertQuoteAction.PickImage -> {
                updateImage(action.path)
                removeQuoteText()
            }

            UpsertQuoteAction.RemoveImage -> removeImage()
            is UpsertQuoteAction.SelectBook -> selectBook(action.book)
            is UpsertQuoteAction.SelectCategory -> updateCategory(action.category)
            is UpsertQuoteAction.ToggleSavingMode -> updateSavingMode(action.isImage)
            is UpsertQuoteAction.UpdateQuote -> updateQuote(action.content)
            is UpsertQuoteAction.ScanTextFromImage -> {
                updateQuote(action.text)
                removeImage()
            }
        }
    }

    private fun saveQuote() = viewModelScope.launch {
        try {
            val field = _uiState.value.field
            val book = _uiState.value.books.firstOrNull { it.bookId == field.bookId }
            val quote = Quote(
                quoteId = "",
                quote = field.quote,
                author = book?.author ?: "",
                book = book?.title ?: "",
                page = 0,
                categoryId = field.categoryId,
                bookId = field.bookId,
                image = field.image
            )
            upsertQuote.invoke(quoteId, quote)
            _eventChannel.send(UpsertQuoteEvent.GoHome)
        } catch (e: InvalidQuoteException) {
            _eventChannel.send(UpsertQuoteEvent.ShowSnackbar(e.message ?: "Failed save quote!"))
        }
    }

    private fun deleteQuote() = viewModelScope.launch {
        if (quoteId != null) {
            quoteRepository.deleteQuote(quoteId)
            _eventChannel.send(UpsertQuoteEvent.GoHome)
        }
    }

    private fun updateQuote(newQuote: String) {
        _uiState.update {
            it.copy(
                field = it.field.copy(quote = newQuote),
            )
        }
    }


    private fun updateImage(newImage: String) {
        _uiState.update {
            it.copy(field = it.field.copy(image = newImage), isSavingAsImage = true)
        }
    }

    private fun removeImage() {
        _uiState.update {
            it.copy(field = it.field.copy(image = ""), isSavingAsImage = false)
        }
    }

    private fun removeQuoteText() {
        _uiState.update {
            it.copy(field = it.field.copy(quote = ""), isSavingAsImage = true)
        }
    }

    private fun updateSavingMode(isImage: Boolean) {
        _uiState.update {
            it.copy(isSavingAsImage = isImage)
        }
    }

    private fun updateCategory(category: Category) {
        _uiState.update {
            it.copy(
                field = it.field.copy(categoryId = category.categoryId),
            )
        }
    }

    private fun selectBook(book: Book?) {
        _uiState.update {
            it.copy(
                field = it.field.copy(bookId = book?.bookId),
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            getDetailQuote.invoke(quoteId).let { data ->
                _uiState.update {
                    var currentBook: String? = null
                    if (bookId != null) {
                        val book = data.books.first { book -> book.bookId == bookId }
                        currentBook = book.bookId
                    }
                    if (data.quote != null) {
                        val categoryId =
                            data.quote.categoryId.ifBlank { data.categories.first().categoryId }
                        it.copy(
                            field = QuoteInputField(
                                quote = data.quote.quote,
                                bookId = data.quote.bookId,
                                categoryId = categoryId,
                                image = data.quote.image
                            ),
                            books = data.books,
                            categories = data.categories,
                            isSavingAsImage = data.quote.image.isNotBlank()
                        )
                    } else {
                        it.copy(
                            field = QuoteInputField(
                                categoryId = data.categories.first().categoryId,
                                bookId = currentBook
                            ),
                            books = data.books,
                            categories = data.categories
                        )
                    }
                }
            }
        }
    }
}