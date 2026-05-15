package com.app.zuludin.buqu.domain.usecase.book

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.InvalidBookException
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpsertBookUseCaseTest {

    @Mock
    private lateinit var bookRepository: IBookRepository

    private lateinit var upsertBookUseCase: UpsertBookUseCase

    @Before
    fun setUp() {
        upsertBookUseCase = UpsertBookUseCase(bookRepository)
    }

    @Test
    fun `invoke with valid book should call repository upsert`() = runTest {
        val book = Book(
            bookId = "1",
            title = "Valid Title",
            author = "Valid Author",
            cover = "",
            description = "Description",
            totalPages = 100,
            publisher = "Publisher",
            year = 2023
        )

        upsertBookUseCase(book.bookId, book)

        verify(bookRepository).upsertBook(
            bookId = book.bookId,
            title = book.title,
            author = book.author,
            cover = book.cover,
            description = book.description,
            totalPages = book.totalPages,
            publisher = book.publisher,
            year = book.year
        )
    }

    @Test(expected = InvalidBookException::class)
    fun `invoke with empty title and author should throw InvalidBookException`() = runTest {
        val book = Book(
            bookId = "1",
            title = "",
            author = "",
            cover = "",
            description = "",
            totalPages = 0,
            publisher = "",
            year = 0
        )
        upsertBookUseCase(book.bookId, book)
    }

    @Test(expected = InvalidBookException::class)
    fun `invoke with empty title should throw InvalidBookException`() = runTest {
        val book = Book(
            bookId = "1",
            title = "",
            author = "Author",
            cover = "",
            description = "",
            totalPages = 0,
            publisher = "",
            year = 0
        )
        upsertBookUseCase(book.bookId, book)
    }

    @Test(expected = InvalidBookException::class)
    fun `invoke with empty author should throw InvalidBookException`() = runTest {
        val book = Book(
            bookId = "1",
            title = "Title",
            author = "",
            cover = "",
            description = "",
            totalPages = 0,
            publisher = "",
            year = 0
        )
        upsertBookUseCase(book.bookId, book)
    }
}
