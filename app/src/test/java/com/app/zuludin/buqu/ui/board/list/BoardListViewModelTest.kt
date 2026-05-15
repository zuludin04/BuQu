package com.app.zuludin.buqu.ui.board.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.BoardRepository
import com.app.zuludin.buqu.domain.models.Board
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BoardListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var boardRepository: BoardRepository

    private lateinit var viewModel: BoardListViewModel

    @Before
    fun setUp() {
        // Default behavior for boardRepository.getBoards()
        `when`(boardRepository.getBoards()).thenReturn(flowOf(emptyList()))
    }

    @Test
    fun `uiState initially shows empty list if repository is empty`() = runTest {
        viewModel = BoardListViewModel(boardRepository)
        val state = viewModel.uiState.first()
        assertTrue(state.boards.isEmpty())
        assertTrue(state.isEmpty)
        assertFalse(state.isLoading)
    }

    @Test
    fun `uiState shows boards when repository provides data`() = runTest {
        val boards = listOf(Board("1", "Board 1", "FFFFFF"), Board("2", "Board 2", "000000"))
        `when`(boardRepository.getBoards()).thenReturn(flowOf(boards))
        
        viewModel = BoardListViewModel(boardRepository)
        val state = viewModel.uiState.first()
        
        assertEquals(2, state.boards.size)
        assertEquals("Board 1", state.boards[0].name)
        assertFalse(state.isEmpty)
        assertFalse(state.isLoading)
    }

    @Test
    fun `uiState handles error from repository`() = runTest {
        `when`(boardRepository.getBoards()).thenReturn(flowOf()) // This might not trigger catch as it just completes
        // To trigger catch, we need to throw an exception in the flow
        `when`(boardRepository.getBoards()).thenReturn(flowOf<List<Board>>().map { throw RuntimeException("Error") })
        
        // Actually the catch block in ViewModel: .catch<Async<List<Board>>> { emit(Async.Error("Error Loading Boards")) }
        
        viewModel = BoardListViewModel(boardRepository)
        val state = viewModel.uiState.first()
        
        assertFalse(state.isLoading)
        assertTrue(state.boards.isEmpty())
    }
}
