package com.app.zuludin.buqu.ui.upsertquote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UpsertQuoteViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var quoteRepo: QuoteRepository

    @Mock
    private lateinit var categoryRepo: CategoryRepository

    private lateinit var viewModel: UpsertQuoteViewModel

    @Test
    fun saveQuote_successSaveNewQuote() = runTest {
        viewModel = UpsertQuoteViewModel(quoteRepo, categoryRepo, SavedStateHandle())

        val quote = "Quote"
        val author = "Awa"
        val book = "Rei"
        val page = "12"
        viewModel.apply {
            updateQuote(quote)
            updateAuthor(author)
            updateBook(book)
            updatePage(page)
        }
        viewModel.saveQuote()
        val state = viewModel.uiState.first()

        verify(quoteRepo).upsertQuote(
            null,
            quote,
            author,
            book,
            page.toInt(),
            "a76c5015-34c7-4a54-bdfb-c5ed2010b7c9"
        )
        assertTrue(state.isQuoteSaved)
        assertFalse(state.isError)
    }

    @Test
    fun saveQuote_errorWhenSaveNewQuote() = runTest {
        viewModel = UpsertQuoteViewModel(quoteRepo, categoryRepo, SavedStateHandle())

        viewModel.saveQuote()
        val state = viewModel.uiState.first()
        assertTrue(state.isError)
    }

    @Test
    fun deleteQuote_successDeleteQuote() = runTest {
        viewModel = UpsertQuoteViewModel(
            quoteRepo,
            categoryRepo,
            SavedStateHandle(mapOf(BuquDestinationArgs.QUOTE_ID_ARG to "0"))
        )
        viewModel.deleteQuote()

        val state = viewModel.uiState.first()

        verify(quoteRepo).deleteQuote("0")
        assertTrue(state.isQuoteSaved)
    }

    @Test
    fun loadQuote_showQuoteById() = runTest {
        val quote = Quote("0", "Hallo", "Asa", "as", 12, "cat1", "000000", "Quote")
        viewModel = UpsertQuoteViewModel(
            quoteRepo,
            categoryRepo,
            SavedStateHandle(mapOf(BuquDestinationArgs.QUOTE_ID_ARG to "0"))
        )
        `when`(quoteRepo.getQuoteById(quote.quoteId)).thenReturn(quote)

        viewModel.loadQuote(quote.quoteId)

        val state = viewModel.uiState.first()

        assertEquals(quote.quote, state.quote)
        assertEquals(quote.author, state.author)
    }
}