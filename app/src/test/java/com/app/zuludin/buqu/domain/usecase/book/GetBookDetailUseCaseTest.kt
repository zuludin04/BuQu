package com.app.zuludin.buqu.domain.usecase.book

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetBookDetailUseCaseTest {

    @Mock
    private lateinit var bookRepository: IBookRepository

    @Mock
    private lateinit var quoteRepository: IQuoteRepository

    private lateinit var getBookDetailUseCase: GetBookDetailUseCase

    @Before
    fun setUp() {
        getBookDetailUseCase = GetBookDetailUseCase(bookRepository, quoteRepository)
    }

    @Test
    fun `invoke should return book detail and filtered quotes`() = runTest {
        val bookId = "book1"
        val book = Book(
            bookId = bookId,
            title = "Book Title",
            author = "Author",
            cover = "",
            description = "Description",
            totalPages = 100,
            publisher = "Publisher",
            year = 2023
        )
        val quotes = listOf(
            Quote("q1", "Quote 1", "A1", "B1", 1, "c1", bookId = bookId),
            Quote("q2", "Quote 2", "A2", "B2", 2, "c2", bookId = "otherBook")
        )

        `when`(bookRepository.getBookById(bookId)).thenReturn(book)
        `when`(quoteRepository.observeQuotes()).thenReturn(flowOf(quotes))

        val result = getBookDetailUseCase(bookId)

        assertNotNull(result.book)
        assertEquals(bookId, result.book?.bookId)
        assertEquals(1, result.quotes.size)
        assertEquals("q1", result.quotes[0].quoteId)
    }
}
