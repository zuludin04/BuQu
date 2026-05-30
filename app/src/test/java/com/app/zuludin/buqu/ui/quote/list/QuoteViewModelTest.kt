package com.app.zuludin.buqu.ui.quote.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class QuoteViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var quoteRepo: QuoteRepository

    @Mock
    private lateinit var categoryRepo: CategoryRepository

    private lateinit var viewModel: QuoteViewModel

    private val quotes = listOf(
        Quote("1", "Quote 1", "Author 1", "Book 1", 10, "cat1"),
        Quote("2", "Funny quote", "Author 2", "Book 2", 20, "cat2")
    )
    private val categories = listOf(
        Category("cat1", "Motivation", "000000", "Quote"),
        Category("cat2", "Funny", "FFFFFF", "Quote")
    )

    @Before
    fun setUp() {
        Mockito.`when`(quoteRepo.observeQuotes()).thenReturn(flowOf(quotes))
        Mockito.`when`(categoryRepo.observeCategories()).thenReturn(flowOf(categories))
        viewModel = QuoteViewModel(quoteRepo, categoryRepo)
    }

    @Test
    fun `uiState initially reflects all quotes and categories`() = runTest {
        val state = viewModel.uiState.first()
        Assert.assertEquals(quotes, state.quotes)
        Assert.assertEquals(categories, state.categories)
        Assert.assertFalse(state.isLoading)
    }

    @Test
    fun `searchQuotes filters by author`() = runTest {
        viewModel.searchQuotes("Author 1")
        val state = viewModel.uiState.first()
        Assert.assertEquals(1, state.quotes.size)
        Assert.assertEquals("1", state.quotes[0].quoteId)
    }
}