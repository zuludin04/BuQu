package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class BoardDaoTest {
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
    fun insertBoard_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val actual = database.boardDao().getById(board.boardId)
        assertNotNull(actual)
        assertEquals(board.boardId, actual?.boardId)
        assertEquals(board.name, actual?.name)
    }

    @Test
    fun deleteBoard_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        database.boardDao().deleteById(board.boardId)

        val actual = database.boardDao().observeAllBoards().first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun observeBoardTotalNote_success() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        database.boardDao().upsert(board)

        val notes = DataDummy.generateNoteCardDummy(board.boardId)
        database.noteCardDao().upsert(notes)

        val actual = database.boardDao().observeBoardTotalNote().first()
        assertNotNull(actual)
        assertEquals(1, actual.size)
        assertEquals(board.boardId, actual[0].boardId)
        assertEquals(notes.size, actual[0].totalNote)
    }
}
