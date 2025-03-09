package com.app.zuludin.buqu.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isResetSuccess: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val categoryRepository: CategoryRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun resetData() = viewModelScope.launch {
        quoteRepository.deleteAllQuote()
        categoryRepository.deleteAllCategory()
        _uiState.update { it.copy(isResetSuccess = true) }
    }

    fun successMessageShown() {
        _uiState.update {
            it.copy(isResetSuccess = false)
        }
    }
}