package com.app.zuludin.buqu.domain.usecase.quote

import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetDetailQuoteUseCaseTest {

    @Mock
    private lateinit var quoteRepository: IQuoteRepository

    @Mock
    private lateinit var bookRepository: IBookRepository

    @Mock
    private lateinit var categoryRepository: ICategoryRepository

    private lateinit var getDetailQuoteUseCase: GetDetailQuoteUseCase

    @Before
    fun setUp() {
        getDetailQuoteUseCase = GetDetailQuoteUseCase(
            quoteRepository,
            bookRepository,
            categoryRepository
        )
    }

    @Test
    fun `invoke with quoteId should return quote detail, books, and categories`() = runTest {
        val quoteId = "Quote#1"
        val quote = Quote(
            quoteId = quoteId,
            quote = "Hallo",
            author = "Asa",
            book = "Qoar",
            page = 10,
            categoryId = "Category1"
        )
        val books = DataDummy.generateBookDummy().toExternal()
        val categories = DataDummy.generateCategoryDummy().toExternal()

        `when`(quoteRepository.getQuoteById(quoteId)).thenReturn(quote)
        `when`(bookRepository.observeBooks()).thenReturn(flowOf(books))
        `when`(categoryRepository.observeCategories()).thenReturn(flowOf(categories))

        val result = getDetailQuoteUseCase(quoteId)

        assertNotNull(result.quote)
        assertEquals(quoteId, result.quote?.quoteId)
        assertEquals(books.size, result.books.size)
        assertEquals(categories.size, result.categories.size)
    }

    @Test
    fun `invoke with null quoteId should return null quote, books, and categories`() = runTest {
        val books = DataDummy.generateBookDummy().toExternal()
        val categories = DataDummy.generateCategoryDummy().toExternal()

        `when`(bookRepository.observeBooks()).thenReturn(flowOf(books))
        `when`(categoryRepository.observeCategories()).thenReturn(flowOf(categories))

        val result = getDetailQuoteUseCase(null)

        assertNull(result.quote)
        assertEquals(books.size, result.books.size)
        assertEquals(categories.size, result.categories.size)
    }
}
