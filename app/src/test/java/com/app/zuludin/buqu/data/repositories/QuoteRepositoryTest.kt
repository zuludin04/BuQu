package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.database.toQuoteAndCategory
import com.app.zuludin.buqu.data.datasources.source.QuoteLocalDataSource
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
import org.mockito.junit.MockitoJUnitRunner

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
    fun getQuotes_successLoadAllQuotes() = runTest {
        val quotes = DataDummy.generateQuoteDummy().map { it.toQuoteAndCategory() }
        val data = flow { emit(quotes) }
        `when`(localSource.getQuotes()).thenReturn(data)

        val actual = repository.getQuotes().first()

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
}