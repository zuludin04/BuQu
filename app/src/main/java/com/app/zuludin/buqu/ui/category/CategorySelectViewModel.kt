package com.app.zuludin.buqu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.colors
import com.app.zuludin.buqu.core.utils.Async
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.domain.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val inUseCategory: Boolean = false,
    val selectedCategory: Category = Category("", "", colors[0], "")
)

@HiltViewModel
class CategorySelectViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _categories = repository.getCategories().map { Async.Success(it) }
        .catch<Async<List<Category>>> { emit(Async.Error("Error Loading Categories")) }
    private val _selectedCategory = MutableStateFlow(Category("", "", colors[0], ""))

    private val _categoryInUse = MutableStateFlow(false)
    val categoryInUse: StateFlow<Boolean> = _categoryInUse.asStateFlow()


    val uiState: StateFlow<CategoriesUiState> =
        combine(
            _categories,
            _selectedCategory
        ) { categories, selectedCategory ->
            when (categories) {
                Async.Loading -> {
                    CategoriesUiState()
                }

                is Async.Error -> {
                    CategoriesUiState()
                }

                is Async.Success -> {
                    CategoriesUiState(
                        categories = categories.data,
                        selectedCategory = selectedCategory
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = CategoriesUiState()
        )

    fun upsertCategory(name: String, color: String) {
        viewModelScope.launch {
            val id =
                if (_selectedCategory.value.categoryId == "") null else _selectedCategory.value.categoryId
            repository.upsertCategory(
                name = name,
                color = color,
                type = "Quote",
                categoryId = id
            )
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            val deleted = repository.deleteCategory(_selectedCategory.value.categoryId)
            _categoryInUse.value = deleted
        }
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }
}