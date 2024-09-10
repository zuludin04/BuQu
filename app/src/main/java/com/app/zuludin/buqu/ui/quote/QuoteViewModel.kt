package com.app.zuludin.buqu.ui.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.usecases.DeleteQuoteUseCase
import com.app.zuludin.buqu.domain.usecases.GetQuotesUseCase
import com.app.zuludin.buqu.domain.usecases.UpsertQuoteUseCase
import com.app.zuludin.buqu.util.Async
import com.app.zuludin.buqu.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuoteUiState(
    val quotes: List<Quote> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

@HiltViewModel
class QuoteViewModel @Inject constructor(
    getQuotesUseCase: GetQuotesUseCase,
    private val upsertQuoteUseCase: UpsertQuoteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase
) :
    ViewModel() {
    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _quotes = getQuotesUseCase.invoke()

    val uiState: StateFlow<QuoteUiState> =
        combine(_isLoading, _userMessage, _quotes) { isLoading, userMessage, quotes ->
            when (quotes) {
                Async.Loading -> {
                    QuoteUiState(isLoading = true)
                }

                is Async.Error -> {
                    QuoteUiState(isLoading = false, userMessage = quotes.errorMessage)
                }

                is Async.Success -> {
                    QuoteUiState(
                        quotes = quotes.data,
                        isLoading = isLoading,
                        userMessage = userMessage
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = QuoteUiState(isLoading = true)
        )

    fun insertQuote(quote: String, author: String) = viewModelScope.launch {
        upsertQuoteUseCase.invoke(quote, author)
        _userMessage.value = "Success insert quote"
    }

    fun deleteQuote(id: String) = viewModelScope.launch {
        deleteQuoteUseCase.invoke(id)
        _userMessage.value = "Success delete quote"
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }
}