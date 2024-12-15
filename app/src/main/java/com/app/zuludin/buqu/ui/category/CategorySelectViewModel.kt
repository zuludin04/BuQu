package com.app.zuludin.buqu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.usecases.GetCategoriesUseCase
import com.app.zuludin.buqu.util.Async
import com.app.zuludin.buqu.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CategorySelectViewModel @Inject constructor(useCase: GetCategoriesUseCase) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _categories = useCase.invoke()

    val uiState: StateFlow<CategoriesUiState> =
        combine(_isLoading, _categories) { isLoading, categories ->
            when (categories) {
                Async.Loading -> {
                    CategoriesUiState(isLoading = true)
                }

                is Async.Error -> {
                    CategoriesUiState(isLoading = false)
                }

                is Async.Success -> {
                    CategoriesUiState(
                        isLoading = isLoading,
                        categories = categories.data
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = CategoriesUiState(isLoading = true)
        )
}