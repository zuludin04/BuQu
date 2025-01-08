package com.app.zuludin.buqu.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.usecases.ResetDatabaseUseCase
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
class SettingsViewModel @Inject constructor(private val useCase: ResetDatabaseUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun resetData() = viewModelScope.launch {
        useCase.invoke()
        _uiState.update { it.copy(isResetSuccess = true) }
    }

    fun successMessageShown() {
        _uiState.update {
            it.copy(isResetSuccess = false)
        }
    }
}