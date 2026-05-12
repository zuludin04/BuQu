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
class RopeDaoTest {
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
    fun insertRopes_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val rope = DataDummy.generateRopeDummy(board.boardId, notes[0].noteId, notes[1].noteId)
        database.ropeDao().upsert(listOf(rope))

        val actual = database.ropeDao().getConnectedRopes(board.boardId)
        assertNotNull(actual)
        assertEquals(1, actual.size)
        assertEquals(rope.ropeId, actual[0].ropeId)
    }

    @Test
    fun deleteRope_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val rope = DataDummy.generateRopeDummy(board.boardId, notes[0].noteId, notes[1].noteId)
        database.ropeDao().upsert(listOf(rope))

        database.ropeDao().deleteById(rope.ropeId)

        val actual = database.ropeDao().getConnectedRopes(board.boardId)
        assertTrue(actual.isEmpty())
    }

    @Test
    fun deleteSelectedRopes_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val rope1 = DataDummy.generateRopeDummy(board.boardId, notes[0].noteId, notes[1].noteId)
        val rope2 = DataDummy.generateRopeDummy(board.boardId, notes[1].noteId, notes[2].noteId).copy(ropeId = "Rope2")
        database.ropeDao().upsert(listOf(rope1, rope2))

        database.ropeDao().deleteSelectedRopes(listOf(rope1))

        val actual = database.ropeDao().getConnectedRopes(board.boardId)
        assertEquals(1, actual.size)
        assertEquals(rope2.ropeId, actual[0].ropeId)
    }
}
