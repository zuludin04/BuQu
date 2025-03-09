package com.app.zuludin.buqu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.Async
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.domain.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CategorySelectViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _categories = repository.getCategories().map { Async.Success(it) }
        .catch<Async<List<Category>>> { emit(Async.Error("Error Loading Categories")) }

    val successDeleteCategory = MutableStateFlow(false)

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
                        categories = categories.data,
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = CategoriesUiState(isLoading = true)
        )

    fun upsertCategory(id: String?, color: String, name: String) {
        viewModelScope.launch {
            repository.upsertCategory(
                name = name,
                color = color,
                type = "Quote",
                categoryId = if (id == "") null else id
            )
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            val deleted = repository.deleteCategory(categoryId)
            successDeleteCategory.value = deleted
        }
    }

    fun messageShown() {
        successDeleteCategory.value = false
    }
}