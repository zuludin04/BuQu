package com.app.zuludin.buqu.ui.board.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.BoardEditorData
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.usecase.board.DeleteBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.GetBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.UpsertBoardUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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

    @Mock
    private lateinit var deleteBoard: DeleteBoardUseCase

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
        viewModel = BoardEditorViewModel(getBoard, upsertBoard, deleteBoard, savedStateHandle)
    }

    @Test
    fun `init loads board data`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(boardId, state.board?.boardId)
        assertEquals("Board 1", state.board?.name)
    }

    @Test
    fun `onAction OnOpenDialog with ImportQuotes updates dialogState`() = runTest {
        viewModel.onAction(BoardEditorAction.OnOpenDialog(BoardDialogState.ImportQuotes))
        val state = viewModel.uiState.first()
        assertEquals(BoardDialogState.ImportQuotes, state.dialogState)
    }

    @Test
    fun `onAction OnOpenDialog with None resets dialogState`() = runTest {
        viewModel.onAction(BoardEditorAction.OnOpenDialog(BoardDialogState.ImportQuotes))
        viewModel.onAction(BoardEditorAction.OnOpenDialog(BoardDialogState.None))
        val state = viewModel.uiState.first()
        assertEquals(BoardDialogState.None, state.dialogState)
    }

    @Test
    fun `onAction OnToggleGrid changes showGrid state`() = runTest {
        val initialGrid = viewModel.uiState.value.showGrid
        viewModel.onAction(BoardEditorAction.OnToggleGrid)
        assertEquals(!initialGrid, viewModel.uiState.value.showGrid)
    }

    @Test
    fun `onAction ConfirmUpsertBoard calls upsertBoard use case`() = runTest {
        val title = "New Board Name"
        val color = "000000"
        viewModel.onAction(BoardEditorAction.ConfirmUpsertBoard(title, color))
        
        verify(upsertBoard).invoke(
            board = Board(boardId, title, color),
            notes = emptyList(),
            ropes = emptyList()
        )
        
        val event = viewModel.events.first()
        assertEquals(BoardEditorEvent.SuccessSaveBoard, event)
    }

    @Test
    fun `onAction ConfirmImportQuotes adds notes from selected quotes`() = runTest {
        val quotes = listOf(
            Quote("q1", "Quote 1", "A1", "B1", 1, "c1", isSelected = true),
            Quote("q2", "Quote 2", "A2", "B2", 2, "c2", isSelected = false)
        )
        
        val boardDataWithQuotes = BoardEditorData(
            board = Board(boardId, "Board 1", "FFFFFF"),
            quotes = quotes
        )
        `when`(getBoard.invoke(boardId)).thenReturn(boardDataWithQuotes)
        // Since loadData is private, we re-instantiate or trigger refresh.
        // Actually loadData is called in init.
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.BOARD_ID_ARG to boardId))
        viewModel = BoardEditorViewModel(getBoard, upsertBoard, deleteBoard, savedStateHandle)
        
        viewModel.onAction(BoardEditorAction.ConfirmImportQuotes(quotes.filter { it.isSelected }))
        
        val state = viewModel.uiState.first()
        assertTrue(state.notes.any { it.title == "Quote 1" })
    }

    @Test
    fun `onAction OnDeleteSelectedNotes marks notes and related ropes as deleted`() = runTest {
        val note1 = NoteCard("n1", boardId, "Note 1", 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, color = "white", image = "")
        val note2 = NoteCard("n2", boardId, "Note 2", 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, color = "white", image = "")
        val rope = Rope("r1", "n1", "n2", boardId, 0f, 0f, androidx.compose.ui.unit.IntSize.Zero, 0f, 0f, androidx.compose.ui.unit.IntSize.Zero)
        
        `when`(getBoard.invoke(boardId)).thenReturn(BoardEditorData(
            board = Board(boardId, "B", "W"),
            notes = listOf(note1, note2),
            ropes = listOf(rope)
        ))
        
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.BOARD_ID_ARG to boardId))
        viewModel = BoardEditorViewModel(getBoard, upsertBoard, deleteBoard, savedStateHandle)
        
        // Select note 1
        viewModel.onAction(BoardEditorAction.OnSelectNote("n1"))
        
        viewModel.onAction(BoardEditorAction.OnDeleteSelectedNotes)
        
        val state = viewModel.uiState.first()
        assertEquals("deleted", state.notes.first { it.noteId == "n1" }.status)
        assertEquals("active", state.notes.first { it.noteId == "n2" }.status)
        assertEquals("deleted", state.ropes.first { it.ropeId == "r1" }.status)
        assertTrue(state.selectedNoteIds.isEmpty())
    }
}
