package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.database.toConnected
import com.app.zuludin.buqu.data.datasources.source.board.BoardLocalDataSource
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
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RopeRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: BoardLocalDataSource

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: RopeRepository

    @Before
    fun setUp() {
        repository = RopeRepository(localSource, testDispatcher)
    }

    @Test
    fun getConnectedRopes_successLoadRopes() = runTest {
        val boardId = "Board1"
        val rope = DataDummy.generateRopeDummy(boardId, "Source1", "Target1").toConnected()
        `when`(localSource.getConnectedRopes(boardId)).thenReturn(listOf(rope))

        val actual = repository.getConnectedRopes(boardId)

        assertNotNull(actual)
        assertEquals(1, actual.size)
        assertEquals(rope.ropeId, actual[0].ropeId)
    }

    @Test
    fun deleteRope_successDeleteRope() = runTest {
        val ropeId = "Rope1"
        repository.deleteRope(ropeId)
        org.mockito.Mockito.verify(localSource).deleteRope(ropeId)
    }
}
