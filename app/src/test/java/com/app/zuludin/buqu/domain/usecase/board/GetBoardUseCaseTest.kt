package com.app.zuludin.buqu.domain.usecase.board

import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetBoardUseCaseTest {

    @Mock
    private lateinit var boardRepository: IBoardRepository

    @Mock
    private lateinit var noteRepository: INoteCardRepository

    @Mock
    private lateinit var ropeRepository: IRopeRepository

    @Mock
    private lateinit var quoteRepository: IQuoteRepository

    @Mock
    private lateinit var bookRepository: IBookRepository

    private lateinit var getBoardUseCase: GetBoardUseCase

    @Before
    fun setUp() {
        getBoardUseCase = GetBoardUseCase(
            boardRepository,
            noteRepository,
            ropeRepository,
            quoteRepository,
            bookRepository
        )
    }

    @Test
    fun `invoke with boardId should return board detail with related data`() = runTest {
        val boardId = "board1"
        val board = Board(boardId, "Board Name", "FFFFFF")
        val quotes = emptyList<Quote>()
        val books = emptyList<Book>()
        val notes = emptyList<NoteCard>()
        val ropes = emptyList<Rope>()

        `when`(quoteRepository.loadQuotes()).thenReturn(quotes)
        `when`(bookRepository.observeBooks()).thenReturn(flowOf(books))
        `when`(boardRepository.getBoardById(boardId)).thenReturn(board)
        `when`(noteRepository.getNotesByBoard(boardId)).thenReturn(notes)
        `when`(ropeRepository.getConnectedRopes(boardId)).thenReturn(ropes)

        val result = getBoardUseCase(boardId)

        assertNotNull(result.board)
        assertEquals(boardId, result.board?.boardId)
        assertEquals(notes, result.notes)
        assertEquals(ropes, result.ropes)
        assertEquals(quotes, result.quotes)
        assertEquals(books, result.books)
    }

    @Test
    fun `invoke with null boardId should return data with null board`() = runTest {
        val quotes = emptyList<Quote>()
        val books = emptyList<Book>()

        `when`(quoteRepository.loadQuotes()).thenReturn(quotes)
        `when`(bookRepository.observeBooks()).thenReturn(flowOf(books))

        val result = getBoardUseCase(null)

        assertNull(result.board)
        assertEquals(quotes, result.quotes)
        assertEquals(books, result.books)
        assertEquals(0, result.notes.size)
        assertEquals(0, result.ropes.size)
    }
}
