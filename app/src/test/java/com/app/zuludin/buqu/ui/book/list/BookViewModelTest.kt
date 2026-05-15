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
import org.junit.Assert.assertTrue
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
        assertEquals(books, state.books)
        assertEquals("", state.query)
        assertEquals(BookSearchScope.Saved, state.scope)
        assertEquals(books, state.savedResults)
    }

    @Test
    fun `onQueryChange updates query and filters saved results`() = runTest {
        viewModel.onQueryChange("Kotlin")
        val state = viewModel.uiState.first()
        assertEquals("Kotlin", state.query)
        assertEquals(1, state.savedResults.size)
        assertEquals("2", state.savedResults[0].bookId)
    }

    @Test
    fun `clearQuery resets query and error message`() = runTest {
        viewModel.onQueryChange("Search")
        viewModel.clearQuery()
        val state = viewModel.uiState.first()
        assertEquals("", state.query)
        assertEquals(books, state.savedResults)
    }

    @Test
    fun `setScope updates scope`() = runTest {
        viewModel.setScope(BookSearchScope.Online)
        val state = viewModel.uiState.first()
        assertEquals(BookSearchScope.Online, state.scope)
    }

    @Test
    fun `searchOnline success updates onlineResults`() = runTest {
        val query = "Online Book"
        val onlineBooks = listOf(Book("3", "Online Book", "Author 3", "", "", 300, "", 2023))
        `when`(bookRepository.searchBooks(query)).thenReturn(onlineBooks)

        viewModel.onQueryChange(query)
        viewModel.searchOnline()

        val state = viewModel.uiState.first()
        assertEquals(onlineBooks, state.onlineResults)
        assertFalse(state.isOnlineLoading)
        assertEquals(null, state.onlineErrorMessage)
    }

    @Test
    fun `searchOnline failure updates onlineErrorMessage`() = runTest {
        val query = "Fail"
        val errorMessage = "Network Error"
        `when`(bookRepository.searchBooks(query)).thenThrow(RuntimeException(errorMessage))

        viewModel.onQueryChange(query)
        viewModel.searchOnline()

        val state = viewModel.uiState.first()
        assertTrue(state.onlineResults.isEmpty())
        assertFalse(state.isOnlineLoading)
        assertEquals(errorMessage, state.onlineErrorMessage)
    }
}
