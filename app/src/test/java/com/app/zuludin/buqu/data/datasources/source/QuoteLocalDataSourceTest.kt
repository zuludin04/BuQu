package com.app.zuludin.buqu.data.datasources.source

import com.app.zuludin.buqu.data.datasources.database.FakeCategoryDao
import com.app.zuludin.buqu.data.datasources.database.FakeQuoteDao
import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuoteLocalDataSourceTest {
    private lateinit var quoteDao: QuoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var localSource: QuoteLocalDataSource

    private val localQuotes = DataDummy.generateQuoteDummy()
    private val localCategories = DataDummy.generateCategoryDummy()

    @Before
    fun setUp() {
        quoteDao = FakeQuoteDao(localQuotes)
        categoryDao = FakeCategoryDao(localCategories)
        localSource = QuoteLocalDataSource(quoteDao, categoryDao)
    }

    @Test
    fun getQuotes_emptyResultFromDatabase() = runTest {
        quoteDao.deleteQuotes()
        val quotes = localSource.getQuotes().first()
        assertTrue(quotes.isEmpty())
    }

    @Test
    fun getQuotes_requestAllQuotesFromDatabase() = runTest {
        val quotes = localSource.getQuotes().first()
        assertTrue(quotes.isNotEmpty())
        assertEquals(localQuotes.size, quotes.size)
    }

    @Test
    fun getQuoteDetail_requestDetailQuoteFromDatabase() = runTest {
        val sample = localQuotes[0]
        val actual = localSource.getQuoteDetail(sample.quoteId)

        assertNotNull(actual)
        assertEquals(sample.quote, actual?.quote)
        assertEquals(sample.author, actual?.author)
    }

    @Test
    fun upsertQuote_successfullyUpsertQuoteIntoDatabase() = runTest {
        val newQuote = QuoteEntity(
            quoteId = "Quote#11",
            quote = "Hallo 11",
            author = "Asa 11",
            book = "Qoar 11",
            page = 12,
            categoryId = "Category11"
        )
        localSource.upsertQuote(newQuote)

        val quote = localSource.getQuoteDetail(newQuote.quoteId)
        assertNotNull(quote)
        assertEquals(newQuote.quote, quote?.quote)
        assertEquals(newQuote.author, quote?.author)
    }

    @Test
    fun deleteQuote_successfullyDeleteQuote() = runTest {
        val initialQuotes = localSource.getQuotes().first()

        localSource.deleteQuote(initialQuotes[0].quoteId)

        val afterDeleteQuote = localSource.getQuotes().first()

        assertEquals(afterDeleteQuote.size, initialQuotes.size - 1)
    }

    @Test
    fun deleteQuotes_removeAllQuotesFromDatabase() = runTest {
        localSource.deleteQuotes()
        val quotes = localSource.getQuotes().first()
        assertTrue(quotes.isEmpty())
    }

    @Test
    fun getCategories_emptyCategoriesFromDatabase() = runTest {
        categoryDao.deleteCategories()
        val categories = localSource.getCategories().first()
        assertTrue(categories.isEmpty())
    }

    @Test
    fun getCategories_requestAllCategoriesFromDatabase() = runTest {
        val categories = localSource.getCategories().first()
        assertTrue(categories.isNotEmpty())
        assertEquals(localCategories, categories)
        assertEquals(localCategories.size, categories.size)
    }

    @Test
    fun getCategoryById_requestDetailCategoryFromDatabase() = runTest {
        val sample = localCategories[0]
        val actual = localSource.getCategoryById(sample.categoryId)
        assertNotNull(actual)
        assertEquals(sample.name, actual?.name)
        assertEquals(sample.type, actual?.type)
    }

    @Test
    fun upsertCategory_successfullyUpsertCategoryIntoDatabase() = runTest {
        val newCategory = CategoryEntity(
            categoryId = "Category11",
            color = "000FF0",
            type = "Quote11",
            name = "Motivation11"
        )
        localSource.upsertCategory(newCategory)

        val category = localSource.getCategoryById(newCategory.categoryId)
        assertNotNull(category)
        assertEquals(newCategory.name, category?.name)
        assertEquals(newCategory.type, category?.type)
    }

    @Test
    fun deleteCategory_successDeleteCategoryFromDatabase() = runTest {
        val initial = localSource.getCategories().first()
        localSource.deleteCategory(initial[0].categoryId)

        val afterDelete = localSource.getCategories().first()

        assertEquals(afterDelete.size, initial.size - 1)
    }

    @Test
    fun deleteCategories_removeAllCategoriesFromDatabase() = runTest {
        localSource.deleteCategories()
        val categories = localSource.getCategories().first()
        assertTrue(categories.isEmpty())
    }
}