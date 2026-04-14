package com.app.zuludin.buqu.ui.board.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.Async
import com.app.zuludin.buqu.core.utils.WhileUiSubscribed
import com.app.zuludin.buqu.data.repositories.BoardRepository
import com.app.zuludin.buqu.domain.models.Board
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class BoardListUiState(
    val boards: List<Board> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)

@HiltViewModel
class BoardListViewModel @Inject constructor(repository: BoardRepository) : ViewModel() {
    private val _boards =
        repository.getBoards().map { Async.Success(it) }
            .catch<Async<List<Board>>> { emit(Async.Error("Error Loading Boards")) }

    val uiState: StateFlow<BoardListUiState> = _boards.map {
        when (it) {
            Async.Loading -> BoardListUiState(isLoading = true)
            is Async.Error -> BoardListUiState(isLoading = false, isEmpty = false)
            is Async.Success -> {
                BoardListUiState(
                    boards = it.data,
                    isLoading = false,
                    isEmpty = it.data.isEmpty()
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = BoardListUiState()
    )
}