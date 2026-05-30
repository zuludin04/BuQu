package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.data.datasources.database.toQuoteAndCategory
import com.app.zuludin.buqu.data.datasources.source.quote.QuoteLocalDataSource
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class QuoteRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: QuoteLocalDataSource

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: QuoteRepository

    @Before
    fun setUp() {
        repository = QuoteRepository(localSource, testDispatcher)
    }

    @Test
    fun observeQuotes_successLoadAllQuotes() = runTest {
        val quotes = DataDummy.generateQuoteDummy().map { it.toQuoteAndCategory() }
        val data = flow { emit(quotes) }
        `when`(localSource.getQuotesCategory()).thenReturn(data)

        val actual = repository.observeQuotes().first()

        assertNotNull(actual)
        assertTrue(actual.isNotEmpty())
        assertEquals(quotes.size, actual.size)
    }

    @Test
    fun getQuoteById_successLoadQuoteDetail() = runTest {
        val quote = DataDummy.generateQuoteDummy()[0]
        `when`(localSource.getQuoteDetail(quote.quoteId)).thenReturn(quote.toQuoteAndCategory())

        val actual = repository.getQuoteById(quote.quoteId)

        assertNotNull(actual)
        assertEquals(quote.quote, actual?.quote)
        assertEquals(quote.author, actual?.author)
    }

    @Test
    fun observeQuotesByCategory_successLoadQuotes() = runTest {
        val quotes = DataDummy.generateQuoteDummy().map { it.toQuoteAndCategory() }
        val filtered = quotes.filter { it.categoryId == "Category1" }
        `when`(localSource.getQuotesByCategory("Category1")).thenReturn(filtered)

        val actual = repository.getQuotesByCategory("Category1")

        assertNotNull(actual)
        assertTrue(actual.isNotEmpty())
        assertEquals(filtered.size, actual.size)
    }

    @Test
    fun upsertQuote_successUpdateQuote() = runTest {
        val quote = DataDummy.generateQuoteDummy()[0]
        `when`(localSource.getQuoteDetail(quote.quoteId)).thenReturn(quote.toQuoteAndCategory())
        
        repository.upsertQuote(quote.quoteId, "New Quote", "Author", "Book", 1, "cat1", "", null)
        
        val expected = QuoteEntity(quote.quoteId, "New Quote", "Author", "Book", 1, "cat1", "", null)
        verify(localSource).upsertQuote(expected)
    }

    @Test
    fun upsertQuote_successSaveNewQuote() = runTest {
        val fixedUuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
        org.mockito.Mockito.mockStatic(UUID::class.java).use { mockedUuid ->
            mockedUuid.`when`<UUID> { UUID.randomUUID() }.thenReturn(fixedUuid)
            
            repository.upsertQuote(null, "Quote", "Author", "Book", 1, "cat1", "", null)
            
            val expected = QuoteEntity(fixedUuid.toString(), "Quote", "Author", "Book", 1, "cat1", "", null)
            verify(localSource).upsertQuote(expected)
        }
    }

    @Test
    fun deleteQuote_successDeleteQuote() = runTest {
        val quoteId = "Quote1"
        repository.deleteQuote(quoteId)
        verify(localSource).deleteQuote(quoteId)
    }
}
