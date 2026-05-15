package com.app.zuludin.buqu.domain.usecase.board

import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpsertBoardUseCaseTest {

    @Mock
    private lateinit var boardRepository: IBoardRepository

    @Mock
    private lateinit var noteRepository: INoteCardRepository

    @Mock
    private lateinit var ropeRepository: IRopeRepository

    private lateinit var upsertBoardUseCase: UpsertBoardUseCase

    @Before
    fun setUp() {
        upsertBoardUseCase = UpsertBoardUseCase(
            boardRepository,
            noteRepository,
            ropeRepository
        )
    }

    @Test
    fun `invoke should call upsert and delete for boards, notes, and ropes`() = runTest {
        val board = Board("board1", "Board Name", "FFFFFF")
        val activeNote = NoteCard(
            noteId = "note1",
            boardId = "board1",
            title = "Note 1",
            posX = 0f,
            posY = 0f,
            size = IntSize(100, 100),
            color = "FFFFFF",
            image = "",
            status = "active"
        )
        val deletedNote = activeNote.copy(noteId = "note2", status = "deleted")
        val notes = listOf(activeNote, deletedNote)

        val activeRope = Rope(
            ropeId = "rope1",
            sourceNoteId = "note1",
            targetNoteId = "note3",
            boardId = "board1",
            sourceX = 0f,
            sourceY = 0f,
            sourceSize = IntSize(0, 0),
            targetX = 0f,
            targetY = 0f,
            targetSize = IntSize(0, 0),
            status = "active"
        )
        val deletedRope = activeRope.copy(ropeId = "rope2", status = "deleted")
        val ropes = listOf(activeRope, deletedRope)

        upsertBoardUseCase(board, notes, ropes)

        verify(boardRepository).upsertBoard(board.boardId, board.name, board.color)
        verify(noteRepository).upsertNotes(listOf(activeNote))
        verify(noteRepository).deleteSelectedNotes(listOf(deletedNote))
        verify(ropeRepository).upsertRopes(listOf(activeRope))
        verify(ropeRepository).deleteSelectedRopes(listOf(deletedRope))
    }
}
