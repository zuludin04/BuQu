package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.BoardTotalNoteEntity
import com.app.zuludin.buqu.data.datasources.source.board.BoardLocalDataSource
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
class BoardRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: BoardLocalDataSource

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: BoardRepository

    @Before
    fun setUp() {
        repository = BoardRepository(localSource, testDispatcher)
    }

    @Test
    fun getBoards_successLoadAllBoards() = runTest {
        val boards = DataDummy.generateBoardDummy()
        val totalNotes = boards.map { BoardTotalNoteEntity(it.boardId, it.name, it.color, 0) }
        `when`(localSource.getBoardTotalNote()).thenReturn(flow { emit(totalNotes) })

        val actual = repository.getBoards().first()

        assertNotNull(actual)
        assertEquals(boards.size, actual.size)
        assertEquals(boards[0].boardId, actual[0].boardId)
    }

    @Test
    fun getBoardById_successLoadBoardDetail() = runTest {
        val board = DataDummy.generateBoardDummy()[0]
        `when`(localSource.getBoardById(board.boardId)).thenReturn(board)

        val actual = repository.getBoardById(board.boardId)

        assertNotNull(actual)
        assertEquals(board.boardId, actual?.boardId)
        assertEquals(board.name, actual?.name)
    }

    @Test
    fun deleteBoard_successDeleteBoard() = runTest {
        val boardId = "Board1"
        repository.deleteBoard(boardId)
        verify(localSource).deleteBoard(boardId)
    }

    @Test
    fun upsertBoard_successUpsertBoard() = runTest {
        val name = "Board Name"
        val color = "FFFFFF"
        val id = repository.upsertBoard(null, name, color)
        val expected = BoardEntity(id, name, color)
        verify(localSource).upsertBoard(expected)
    }
}
