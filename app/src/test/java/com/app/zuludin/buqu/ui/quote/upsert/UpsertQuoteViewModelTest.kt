package com.app.zuludin.buqu.ui.quote.upsert

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.QuoteUpsertData
import com.app.zuludin.buqu.domain.usecase.quote.GetDetailQuoteUseCase
import com.app.zuludin.buqu.domain.usecase.quote.UpsertQuoteUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class UpsertQuoteViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var quoteRepository: QuoteRepository

    @Mock
    private lateinit var getDetailQuote: GetDetailQuoteUseCase

    @Mock
    private lateinit var upsertQuote: UpsertQuoteUseCase

    private lateinit var viewModel: UpsertQuoteViewModel
    private val quoteId = "quote1"
    private val categories = listOf(Category("cat1", "Motivation", "000000", "Quote"))
    private val quote = Quote(quoteId, "Content", "Author", "Book", 1, "cat1")

    @Before
    fun setUp() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(BuquDestinationArgs.QUOTE_ID_ARG to quoteId))
        Mockito.`when`(getDetailQuote.invoke(quoteId))
            .thenReturn(QuoteUpsertData(quote, emptyList(), categories))
        viewModel =
            UpsertQuoteViewModel(quoteRepository, getDetailQuote, upsertQuote, savedStateHandle)
    }

    @Test
    fun `init loads quote data`() = runTest {
        val state = viewModel.uiState.first()
        Assert.assertEquals(quote.quote, state.field.quote)
        Assert.assertEquals(quote.categoryId, state.field.categoryId)
    }

    @Test
    fun `onAction UpdateQuote updates content`() = runTest {
        viewModel.onAction(UpsertQuoteAction.UpdateQuote("New Content"))
        val state = viewModel.uiState.first()
        Assert.assertEquals("New Content", state.field.quote)
    }

    @Test
    fun `onAction SelectCategory updates category`() = runTest {
        val newCat = Category("cat2", "Funny", "FFFFFF", "Quote")
        viewModel.onAction(UpsertQuoteAction.SelectCategory(newCat))
        val state = viewModel.uiState.first()
        Assert.assertEquals("cat2", state.field.categoryId)
    }

    @Test
    fun `onAction SaveQuote calls upsertQuote use case and sends GoHome event`() = runTest {
        val expectedQuote = Quote(
            quoteId = "",
            quote = quote.quote,
            author = "",
            book = "",
            page = 0,
            categoryId = quote.categoryId,
            bookId = quote.bookId,
            image = quote.image
        )

        viewModel.onAction(UpsertQuoteAction.SaveQuote)

        Mockito.verify(upsertQuote).invoke(quoteId, expectedQuote)
        val event = viewModel.events.first()
        Assert.assertEquals(UpsertQuoteEvent.GoHome, event)
    }

    @Test
    fun `onAction DeleteQuote calls repository and sends GoHome event`() = runTest {
        viewModel.onAction(UpsertQuoteAction.DeleteQuote)
        Mockito.verify(quoteRepository).deleteQuote(quoteId)
        val event = viewModel.events.first()
        Assert.assertEquals(UpsertQuoteEvent.GoHome, event)
    }
}