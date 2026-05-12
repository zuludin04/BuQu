package com.app.zuludin.buqu.data.datasources.source.book

import com.app.zuludin.buqu.data.datasources.database.FakeBookDao
import com.app.zuludin.buqu.data.datasources.database.dao.BookDao
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookLocalDataSourceTest {
    private lateinit var bookDao: BookDao
    private lateinit var localSource: BookLocalDataSource

    private val localBooks = DataDummy.generateBookDummy()

    @Before
    fun setUp() {
        bookDao = FakeBookDao(localBooks)
        localSource = BookLocalDataSource(bookDao)
    }

    @Test
    fun getBooks_emptyResultFromDatabase() = runTest {
        bookDao.deleteBooks()
        val books = localSource.getBooks().first()
        assertTrue(books.isEmpty())
    }

    @Test
    fun getBooks_requestAllBooksFromDatabase() = runTest {
        val books = localSource.getBooks().first()
        assertTrue(books.isNotEmpty())
        assertEquals(localBooks.size, books.size)
    }

    @Test
    fun getBookById_requestDetailBookFromDatabase() = runTest {
        val sample = localBooks[0]
        val actual = localSource.getBookById(sample.bookId)

        assertNotNull(actual)
        assertEquals(sample.title, actual?.title)
        assertEquals(sample.author, actual?.author)
    }

    @Test
    fun upsertBook_successfullyUpsertBookIntoDatabase() = runTest {
        val newBook = BookEntity(
            bookId = "Book#6",
            title = "Book Title 6",
            author = "Author 6",
            cover = "",
            description = "Description 6",
            totalPages = 100,
            publisher = "Publisher 6",
            year = 2023
        )
        localSource.upsertBook(newBook)

        val book = localSource.getBookById(newBook.bookId)
        assertNotNull(book)
        assertEquals(newBook.title, book?.title)
        assertEquals(newBook.author, book?.author)
    }

    @Test
    fun deleteBook_successfullyDeleteBook() = runTest {
        val initialBooks = localSource.getBooks().first()

        localSource.deleteBook(initialBooks[0].bookId)

        val afterDeleteBook = localSource.getBooks().first()

        assertEquals(afterDeleteBook.size, initialBooks.size - 1)
    }

    @Test
    fun deleteBooks_removeAllBooksFromDatabase() = runTest {
        localSource.deleteBooks()
        val books = localSource.getBooks().first()
        assertTrue(books.isEmpty())
    }
}
