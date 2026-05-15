package com.app.zuludin.buqu.ui.board.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.BoardEditorData
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.usecase.board.GetBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.UpsertBoardUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BoardEditorViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getBoard: GetBoardUseCase

    @Mock
    private lateinit var upsertBoard: UpsertBoardUseCase

    private lateinit var viewModel: BoardEditorViewModel
    private val boardId = "board1"

    @Before
    fun setUp() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.BOARD_ID_ARG to boardId))
        val boardData = BoardEditorData(
            board = Board(boardId, "Board 1", "FFFFFF"),
            notes = emptyList(),
            ropes = emptyList(),
            quotes = emptyList(),
            books = emptyList()
        )
        `when`(getBoard.invoke(boardId)).thenReturn(boardData)
        viewModel = BoardEditorViewModel(getBoard, upsertBoard, savedStateHandle)
    }

    @Test
    fun `init loads board data`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(boardId, state.board?.boardId)
        assertEquals("Board 1", state.board?.name)
    }

    @Test
    fun `onAction OnOpenQuoteDialog updates dialogState`() = runTest {
        viewModel.onAction(BoardEditorAction.OnOpenQuoteDialog)
        val state = viewModel.uiState.first()
        assertEquals(BoardDialogState.ImportQuotes, state.dialogState)
    }

    @Test
    fun `onAction DismissDialog resets dialogState`() = runTest {
        viewModel.onAction(BoardEditorAction.OnOpenQuoteDialog)
        viewModel.onAction(BoardEditorAction.DismissDialog)
        val state = viewModel.uiState.first()
        assertEquals(BoardDialogState.None, state.dialogState)
    }

    @Test
    fun `toggleGrid changes showGrid state`() = runTest {
        val initialGrid = viewModel.uiState.value.showGrid
        viewModel.toggleGrid()
        assertEquals(!initialGrid, viewModel.uiState.value.showGrid)
    }

    @Test
    fun `onAction OnSaveBoard calls upsertBoard use case`() = runTest {
        val title = "New Board Name"
        val color = "000000"
        viewModel.onAction(BoardEditorAction.OnSaveBoard(title, color))
        
        verify(upsertBoard).invoke(
            board = Board(boardId, title, color),
            notes = emptyList(),
            ropes = emptyList()
        )
        
        val event = viewModel.events.first()
        assertEquals(BoardEditorEvent.SuccessSaveBoard, event)
    }

    @Test
    fun `importQuotes adds notes from selected quotes`() = runTest {
        val quotes = listOf(
            Quote("q1", "Quote 1", "A1", "B1", 1, "c1", isSelected = true),
            Quote("q2", "Quote 2", "A2", "B2", 2, "c2", isSelected = false)
        )
        // Manually update state with quotes because loadData might have been called with empty
        // Actually we can mock getBoard to return these quotes
        
        // Let's just update the internal state if possible, but it's private.
        // Better to reload data with quotes.
        val boardDataWithQuotes = BoardEditorData(
            board = Board(boardId, "Board 1", "FFFFFF"),
            quotes = quotes
        )
        `when`(getBoard.invoke(boardId)).thenReturn(boardDataWithQuotes)
        viewModel.loadData(boardId)
        
        viewModel.onAction(BoardEditorAction.OnImportQuotes)
        
        val state = viewModel.uiState.first()
        assertEquals(1, state.notes.size)
        assertEquals("Quote 1", state.notes[0].title)
        // Ensure quotes selection is reset
        assertFalse(state.quotes.any { it.isSelected })
    }

    @Test
    fun `deleteSelectedNotes marks notes and related ropes as deleted`() = runTest {
        val note1 = NoteCard("n1", boardId, "Note 1", 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, color = "white", image = "")
        val note2 = NoteCard("n2", boardId, "Note 2", 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, color = "white", image = "")
        val rope = Rope("r1", "n1", "n2", boardId, 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, 0f, 0f, androidx.compose.ui.unit.IntSize.Zero)
        
        // Mock loading these
        `when`(getBoard.invoke(boardId)).thenReturn(BoardEditorData(
            board = Board(boardId, "B", "W"),
            notes = listOf(note1, note2),
            ropes = listOf(rope)
        ))
        viewModel.loadData(boardId)
        
        // Select note 1
        viewModel.changeNoteSelectionStatus("n1")
        
        viewModel.deleteSelectedNotes()
        
        val state = viewModel.uiState.first()
        assertEquals("deleted", state.notes.first { it.noteId == "n1" }.status)
        assertEquals("active", state.notes.first { it.noteId == "n2" }.status)
        assertEquals("deleted", state.ropes.first { it.ropeId == "r1" }.status)
        assertTrue(state.selectedNoteIds.isEmpty())
    }
}
