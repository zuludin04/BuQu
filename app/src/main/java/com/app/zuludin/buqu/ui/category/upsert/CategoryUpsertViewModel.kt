//package com.app.zuludin.buqu.ui.category.upsert
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.app.zuludin.buqu.domain.usecases.UpsertCategoryUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//data class UpsertCategoryUiState(
//    val name: String = "",
//    val color: String = "",
//    val type: String = "",
//)
//
//@HiltViewModel
//class CategoryUpsertViewModel @Inject constructor(private val upsertUseCase: UpsertCategoryUseCase) :
//    ViewModel() {
//    private val _uiState = MutableStateFlow(UpsertCategoryUiState())
//    val uiState: StateFlow<UpsertCategoryUiState> = _uiState
//
//    fun saveCategory() = viewModelScope.launch {
//        val state = uiState.value
//        if (state.quote.isNotEmpty() &&
//            state.book.isNotEmpty() &&
//            state.author.isNotEmpty() &&
//            state.page.isNotEmpty()
//        ) {
//            upsertUseCase.invoke(
//                categoryId = null,
//                name = state.name,
//                color = state.color,
//                type = state.type,
//            )
////            _uiState.update {
////                it.copy(isQuoteSaved = true, isError = false)
////            }
//        } else {
////            _uiState.update {
////                it.copy(isError = true)
////            }
//        }
//    }
//}