package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.database.toConnected
import com.app.zuludin.buqu.data.datasources.source.board.BoardLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class NoteCardRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: BoardLocalDataSource

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: NoteCardRepository

    @Before
    fun setUp() {
        repository = NoteCardRepository(localSource, testDispatcher)
    }

    @Test
    fun getNotesByBoard_successLoadNotes() = runTest {
        val boardId = "Board1"
        val notes = DataDummy.generateNoteCardDummy(boardId).map { it.toConnected() }
        `when`(localSource.getNotesByBoard(boardId)).thenReturn(notes)

        val actual = repository.getNotesByBoard(boardId)

        assertNotNull(actual)
        assertEquals(notes.size, actual.size)
    }

    @Test
    fun deleteNote_successDeleteNote() = runTest {
        val noteId = "Note1"
        repository.deleteNote(noteId)
        verify(localSource).deleteNote(noteId)
    }

    @Test
    fun upsertNotes_successUpsertNotes() = runTest {
        val boardId = "Board1"
        val notes = DataDummy.generateNoteCardDummy(boardId).map { it.toConnected().toExternal() }
        repository.upsertNotes(notes)
        verify(localSource).upsertNotes(notes.toLocal())
    }

    @Test
    fun deleteSelectedNotes_successDeleteSelectedNotes() = runTest {
        val boardId = "Board1"
        val notes = DataDummy.generateNoteCardDummy(boardId).map { it.toConnected().toExternal() }
        repository.deleteSelectedNotes(notes)
        verify(localSource).deleteSelectedNotes(notes.toLocal())
    }
}
