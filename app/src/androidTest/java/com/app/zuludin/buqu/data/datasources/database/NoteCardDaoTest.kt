package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteCardDaoTest {
    private lateinit var database: BuQuDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BuQuDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertNotes_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val actual = database.noteCardDao().getNotesByBoard(board.boardId)
        assertNotNull(actual)
        assertEquals(notes.size, actual.size)
    }

    @Test
    fun deleteNote_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        database.noteCardDao().deleteById(notes[0].noteId)

        val actual = database.noteCardDao().getNotesByBoard(board.boardId)
        assertEquals(notes.size - 1, actual.size)
    }

    @Test
    fun deleteSelectedNotes_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        database.noteCardDao().deleteSelectedNotes(listOf(notes[0], notes[1]))

        val actual = database.noteCardDao().getNotesByBoard(board.boardId)
        assertEquals(notes.size - 2, actual.size)
    }

    @Test
    fun observeNotesWithConnection_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val rope = DataDummy.generateRopeDummy(board.boardId, notes[0].noteId, notes[1].noteId)
        database.ropeDao().upsert(listOf(rope))

        val actual = database.noteCardDao().getNotesByBoard(board.boardId)
        assertNotNull(actual)
        
        val connectedNotes = actual.filter { it.isConnected == 1 }
        assertEquals(2, connectedNotes.size)
        assertTrue(connectedNotes.any { it.noteId == notes[0].noteId })
        assertTrue(connectedNotes.any { it.noteId == notes[1].noteId })
    }
}
