package com.app.zuludin.buqu.data.datasources.source.board

import com.app.zuludin.buqu.data.datasources.database.FakeBoardDao
import com.app.zuludin.buqu.data.datasources.database.FakeNoteCardDao
import com.app.zuludin.buqu.data.datasources.database.FakeRopeDao
import com.app.zuludin.buqu.data.datasources.database.dao.BoardDao
import com.app.zuludin.buqu.data.datasources.database.dao.NoteCardDao
import com.app.zuludin.buqu.data.datasources.database.dao.RopeDao
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BoardLocalDataSourceTest {
    private lateinit var boardDao: BoardDao
    private lateinit var noteCardDao: NoteCardDao
    private lateinit var ropeDao: RopeDao
    private lateinit var localSource: BoardLocalDataSource

    private val localBoards = DataDummy.generateBoardDummy()
    private val boardId = localBoards[0].boardId
    private val localNotes = DataDummy.generateNoteCardDummy(boardId)

    @Before
    fun setUp() {
        boardDao = FakeBoardDao(localBoards)
        noteCardDao = FakeNoteCardDao(localNotes)
        ropeDao = FakeRopeDao()
        localSource = BoardLocalDataSource(boardDao, noteCardDao, ropeDao)
    }

    @Test
    fun getBoards_requestAllBoardsFromDatabase() = runTest {
        val boards = localSource.getBoards().first()
        assertTrue(boards.isNotEmpty())
        assertEquals(localBoards.size, boards.size)
    }

    @Test
    fun getBoardById_requestDetailBoardFromDatabase() = runTest {
        val sample = localBoards[0]
        val actual = localSource.getBoardById(sample.boardId)
        assertNotNull(actual)
        assertEquals(sample.name, actual?.name)
    }

    @Test
    fun upsertBoard_successfullyUpsertBoardIntoDatabase() = runTest {
        val newBoard = BoardEntity("Board#6", "Board 6", "000000")
        localSource.upsertBoard(newBoard)

        val board = localSource.getBoardById(newBoard.boardId)
        assertNotNull(board)
        assertEquals(newBoard.name, board?.name)
    }

    @Test
    fun deleteBoard_successfullyDeleteBoard() = runTest {
        val initial = localSource.getBoards().first()
        localSource.deleteBoard(initial[0].boardId)
        val after = localSource.getBoards().first()
        assertEquals(after.size, initial.size - 1)
    }

    @Test
    fun getNotesByBoard_requestAllNotesByBoardFromDatabase() = runTest {
        val notes = localSource.getNotesByBoard(boardId)
        assertEquals(localNotes.size, notes.size)
    }

    @Test
    fun upsertNotes_successfullyUpsertNotesIntoDatabase() = runTest {
        val newNote = NoteCardEntity("Note#6", "Note 6", 0f, 0f, "FFFFFF", 100, 100, boardId, "")
        localSource.upsertNotes(listOf(newNote))

        val notes = localSource.getNotesByBoard(boardId)
        assertTrue(notes.any { it.noteId == newNote.noteId })
    }

    @Test
    fun deleteNote_successfullyDeleteNote() = runTest {
        val initial = localSource.getNotesByBoard(boardId)
        localSource.deleteNote(initial[0].noteId)
        val after = localSource.getNotesByBoard(boardId)
        assertEquals(after.size, initial.size - 1)
    }

    @Test
    fun getConnectedRopes_requestAllConnectedRopesFromDatabase() = runTest {
        val rope = DataDummy.generateRopeDummy(boardId, localNotes[0].noteId, localNotes[1].noteId)
        localSource.upsertRopes(listOf(rope))

        val ropes = localSource.getConnectedRopes(boardId)
        assertEquals(1, ropes.size)
        assertEquals(rope.ropeId, ropes[0].ropeId)
    }

    @Test
    fun deleteRope_successfullyDeleteRope() = runTest {
        val rope = DataDummy.generateRopeDummy(boardId, localNotes[0].noteId, localNotes[1].noteId)
        localSource.upsertRopes(listOf(rope))

        localSource.deleteRope(rope.ropeId)
        val ropes = localSource.getConnectedRopes(boardId)
        assertTrue(ropes.isEmpty())
    }

    @Test
    fun deleteSelectedNotes_successfullyDeleteSelectedNotes() = runTest {
        val selected = listOf(localNotes[0], localNotes[1])
        localSource.deleteSelectedNotes(selected)
        val notes = localSource.getNotesByBoard(boardId)
        assertEquals(localNotes.size - 2, notes.size)
    }

    @Test
    fun deleteSelectedRopes_successfullyDeleteSelectedRopes() = runTest {
        val rope1 = DataDummy.generateRopeDummy(boardId, localNotes[0].noteId, localNotes[1].noteId)
        val rope2 = DataDummy.generateRopeDummy(boardId, localNotes[1].noteId, localNotes[2].noteId)
            .copy(ropeId = "Rope2")
        localSource.upsertRopes(listOf(rope1, rope2))

        localSource.deleteSelectedRopes(listOf(rope1))
        val ropes = localSource.getConnectedRopes(boardId)
        assertEquals(1, ropes.size)
        assertEquals(rope2.ropeId, ropes[0].ropeId)
    }

    @Test
    fun getBoardTotalNote_requestAllBoardTotalNoteFromDatabase() = runTest {
        val result = localSource.getBoardTotalNote().first()
        assertEquals(localBoards.size, result.size)
    }
}
