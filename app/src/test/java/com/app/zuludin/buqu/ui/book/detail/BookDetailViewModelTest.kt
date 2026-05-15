package com.app.zuludin.buqu.ui.book.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.BookDetailData
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.usecase.book.GetBookDetailUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BookDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var bookRepository: IBookRepository

    @Mock
    private lateinit var getBookDetail: GetBookDetailUseCase

    private lateinit var viewModel: BookDetailViewModel
    private val bookId = "book1"
    private val book = Book(bookId, "Title", "Author", "", "", 100, "", 2023, fromDatabase = true)

    @Before
    fun setUp() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.BOOK_ID_ARG to bookId))
        `when`(getBookDetail.invoke(bookId)).thenReturn(BookDetailData(book, emptyList()))
        viewModel = BookDetailViewModel(bookRepository, getBookDetail, savedStateHandle)
    }

    @Test
    fun `init loads book detail`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(book, state.book)
        assertFalse(state.isLoading)
        assertTrue(state.fromDatabase)
    }

    @Test
    fun `saveBook calls repository upsert`() = runTest {
        viewModel.saveBook()
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

    @Test
    fun `deleteBook calls repository delete and sends GoHome event`() = runTest {
        viewModel.deleteBook()
        verify(bookRepository).deleteBook(bookId)
        val event = viewModel.events.first()
        assertEquals(BookDetailEvent.GoHome, event)
    }
}
