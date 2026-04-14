package com.app.zuludin.buqu.ui.board.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.ui.quote.TasksEmptyContent

@Composable
fun BoardListScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardListViewModel = hiltViewModel(),
    onBoardClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BuQuToolbar(title = stringResource(R.string.app_name))
        }
    ) { paddingValues ->
        BoardListContent(
            modifier = Modifier.padding(paddingValues),
            boards = uiState.boards,
            isLoading = uiState.isLoading,
            onBoardClick = onBoardClick
        )
    }
}

@Composable
private fun BoardListContent(
    modifier: Modifier = Modifier,
    boards: List<Board>,
    isLoading: Boolean,
    onBoardClick: (String) -> Unit
) {
    if (boards.isNotEmpty() && !isLoading) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(boards) { board ->
                Text(board.name, modifier = Modifier.clickable { onBoardClick(board.boardId) })
            }
        }
    } else if (isLoading) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    } else {
        TasksEmptyContent()
    }
}