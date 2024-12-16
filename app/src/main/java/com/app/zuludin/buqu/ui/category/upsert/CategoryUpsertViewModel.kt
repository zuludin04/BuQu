package com.app.zuludin.buqu.ui.category.upsert

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.domain.usecases.DeleteCategoryUseCase
import com.app.zuludin.buqu.domain.usecases.GetCategoryDetailUseCase
import com.app.zuludin.buqu.domain.usecases.UpsertCategoryUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpsertCategoryUiState(
    val name: String = "",
    val color: String = "",
//    val type: String = "",
    val isCategorySaved: Boolean = false,
    val isError: Boolean = false,
)

@HiltViewModel
class CategoryUpsertViewModel @Inject constructor(
    private val upsertUseCase: UpsertCategoryUseCase,
    private val deleteUseCase: DeleteCategoryUseCase,
    private val detailUseCase: GetCategoryDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val categoryId: String? = savedStateHandle[BuquDestinationArgs.CATEGORY_ID_ARG]

    private val _uiState = MutableStateFlow(UpsertCategoryUiState())
    val uiState: StateFlow<UpsertCategoryUiState> = _uiState

    init {
        if (categoryId != null) {
            loadCategory(categoryId)
        }
    }

    fun saveCategory() = viewModelScope.launch {
        val state = uiState.value
        if (state.name.isNotEmpty() &&
            state.color.isNotEmpty()
        ) {
            upsertUseCase.invoke(
                categoryId = categoryId,
                name = state.name,
                color = state.color,
                type = "Quote",
            )
            _uiState.update {
                it.copy(isCategorySaved = true, isError = false)
            }
        } else {
            _uiState.update {
                it.copy(isError = true)
            }
        }
    }

    fun deleteCategory() = viewModelScope.launch {
        if (categoryId != null) {
            deleteUseCase.invoke(categoryId)
            _uiState.update {
                it.copy(isCategorySaved = true)
            }
        }
    }

    fun updateName(newName: String) {
        _uiState.update {
            it.copy(name = newName)
        }
    }

    fun updateColor(newColor: String) {
        _uiState.update {
            it.copy(color = newColor)
        }
    }

    fun errorMessageShown() {
        _uiState.update {
            it.copy(isError = false)
        }
    }

    private fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            detailUseCase.invoke(categoryId).let { category ->
                if (category != null) {
                    _uiState.update {
                        it.copy(
                            name = category.name,
                            color = category.color,
                        )
                    }
                }
            }

        }
    }
}