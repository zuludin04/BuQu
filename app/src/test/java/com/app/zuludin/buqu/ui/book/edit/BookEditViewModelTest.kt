package com.app.zuludin.buqu.ui.book.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.usecase.book.UpsertBookUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
class BookEditViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var bookRepository: IBookRepository

    @Mock
    private lateinit var upsertBook: UpsertBookUseCase

    private lateinit var viewModel: BookEditViewModel
    private val bookId = "book1"
    private val book = Book(bookId, "Title", "Author", "Cover", "Desc", 100, "Pub", 2023)

    @Before
    fun setUp() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.BOOK_ID_ARG to bookId))
        `when`(bookRepository.getBookById(bookId)).thenReturn(book)
        viewModel = BookEditViewModel(bookRepository, upsertBook, savedStateHandle)
    }

    @Test
    fun `init loads book for editing`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(bookId, state.bookId)
        assertEquals(book.title, state.title)
        assertEquals(book.author, state.author)
    }

    @Test
    fun `field changes update uiState`() = runTest {
        viewModel.onTitleChange("New Title")
        viewModel.onAuthorChange("New Author")
        viewModel.onTotalPagesChange("200")
        
        val state = viewModel.uiState.first()
        assertEquals("New Title", state.title)
        assertEquals("New Author", state.author)
        assertEquals(200, state.totalPages)
    }

    @Test
    fun `saveBook calls upsertBook use case and sends GoHome event`() = runTest {
        viewModel.onTitleChange("Updated Title")

        val expectedBook = Book(
            bookId = "",
            title = "Updated Title",
            author = book.author,
            cover = book.cover,
            description = book.description,
            totalPages = book.totalPages,
            publisher = book.publisher,
            year = book.year
        )

        viewModel.saveBook()

        verify(upsertBook).invoke(bookId, expectedBook)

        val event = viewModel.events.first()
        assertEquals(BookEditEvent.GoHome, event)
    }
}
