package com.app.zuludin.buqu.ui.book.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BookViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var bookRepository: IBookRepository

    private lateinit var viewModel: BookViewModel

    private val books = listOf(
        Book("1", "Android Development", "Author 1", "", "", 100, "", 2021),
        Book("2", "Kotlin Programming", "Author 2", "", "", 200, "", 2022)
    )

    @Before
    fun setUp() {
        `when`(bookRepository.observeBooks()).thenReturn(flowOf(books))
        viewModel = BookViewModel(bookRepository)
    }

    @Test
    fun `uiState initially reflects observed books and default values`() = runTest {
        val state = viewModel.uiState.first()
        assertEquals(books, state.bookDatabase.books)
        assertEquals("", state.query)
        assertEquals(BookSearchScope.Saved, state.scope)
    }

    @Test
    fun `onAction SearchBooks updates query and filters saved results when scope is Saved`() = runTest {
        viewModel.onAction(BookAction.SearchBooks("Kotlin"))
        val state = viewModel.uiState.first()
        assertEquals("Kotlin", state.query)
        assertEquals(1, state.bookDatabase.books.size)
        assertEquals("2", state.bookDatabase.books[0].bookId)
    }

    @Test
    fun `onAction ClearQuery resets query and database results`() = runTest {
        viewModel.onAction(BookAction.SearchBooks("Search"))
        viewModel.onAction(BookAction.ClearQuery)
        val state = viewModel.uiState.first()
        assertEquals("", state.query)
        assertEquals(books, state.bookDatabase.books)
    }

    @Test
    fun `onAction ChangeScope updates scope`() = runTest {
        viewModel.onAction(BookAction.ChangeScope(BookSearchScope.Online))
        val state = viewModel.uiState.first()
        assertEquals(BookSearchScope.Online, state.scope)
    }

    @Test
    fun `onAction SearchBooks with scope Online updates online results`() = runTest {
        val query = "Online Book"
        val onlineBooks = listOf(Book("3", "Online Book", "Author 3", "", "", 300, "", 2023))
        `when`(bookRepository.searchBooks(query)).thenReturn(onlineBooks)

        viewModel.onAction(BookAction.ChangeScope(BookSearchScope.Online))
        viewModel.onAction(BookAction.SearchBooks(query))

        val state = viewModel.uiState.first()
        assertEquals(onlineBooks, state.bookOnline.books)
        assertFalse(state.bookOnline.isLoading)
    }

    @Test
    fun `onAction BookSearchCta updates scope to Online and triggers search`() = runTest {
        val query = "Cta search"
        val onlineBooks = listOf(Book("4", "Cta Book", "Author 4", "", "", 400, "", 2024))
        `when`(bookRepository.searchBooks(query)).thenReturn(onlineBooks)

        // Set query first in Saved scope
        viewModel.onAction(BookAction.SearchBooks(query))
        
        // Trigger CTA
        viewModel.onAction(BookAction.BookSearchCta(query))

        val state = viewModel.uiState.first()
        assertEquals(BookSearchScope.Online, state.scope)
        assertEquals(onlineBooks, state.bookOnline.books)
        assertFalse(state.bookOnline.isLoading)
    }
}
