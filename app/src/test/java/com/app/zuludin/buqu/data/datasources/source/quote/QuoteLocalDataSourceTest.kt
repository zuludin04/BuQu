package com.app.zuludin.buqu.data.datasources.source.quote

import com.app.zuludin.buqu.data.datasources.database.FakeCategoryDao
import com.app.zuludin.buqu.data.datasources.database.FakeQuoteDao
import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
    fun getQuotes_Category_emptyResultFromDatabase() = runTest {
        quoteDao.deleteQuotes()
        val quotes = localSource.getQuotesCategory().first()
        Assert.assertTrue(quotes.isEmpty())
    }

    @Test
    fun getQuotes_requestAllQuotesCategoryFromDatabase() = runTest {
        val quotes = localSource.getQuotesCategory().first()
        Assert.assertTrue(quotes.isNotEmpty())
        Assert.assertEquals(localQuotes.size, quotes.size)
    }

    @Test
    fun getQuotesByCategory_requestQuotesCategoryFromDatabase() = runTest {
        val sample = localQuotes.filter { it.categoryId == "Category1" }
        val quotes = localSource.getQuotesByCategory("Category1")
        Assert.assertNotNull(quotes)
        Assert.assertEquals(sample.size, quotes.size)
    }

    @Test
    fun getQuoteDetail_requestDetailQuoteFromDatabase() = runTest {
        val sample = localQuotes[0]
        val actual = localSource.getQuoteDetail(sample.quoteId)

        Assert.assertNotNull(actual)
        Assert.assertEquals(sample.quote, actual?.quote)
        Assert.assertEquals(sample.author, actual?.author)
    }

    @Test
    fun upsertQuote_successfullyUpsertQuoteIntoDatabase() = runTest {
        val newQuote = QuoteEntity(
            quoteId = "Quote#11",
            quote = "Hallo 11",
            author = "Asa 11",
            book = "Qoar 11",
            page = 12,
            categoryId = "Category11",
            image = ""
        )
        localSource.upsertQuote(newQuote)

        val quote = localSource.getQuoteDetail(newQuote.quoteId)
        Assert.assertNotNull(quote)
        Assert.assertEquals(newQuote.quote, quote?.quote)
        Assert.assertEquals(newQuote.author, quote?.author)
    }

    @Test
    fun deleteQuote_successfullyDeleteQuote() = runTest {
        val initialQuotes = localSource.getQuotesCategory().first()

        localSource.deleteQuote(initialQuotes[0].quoteId)

        val afterDeleteQuote = localSource.getQuotesCategory().first()

        Assert.assertEquals(afterDeleteQuote.size, initialQuotes.size - 1)
    }

    @Test
    fun deleteQuotes_removeAllQuotesFromDatabase() = runTest {
        localSource.deleteQuotes()
        val quotes = localSource.getQuotesCategory().first()
        Assert.assertTrue(quotes.isEmpty())
    }

    @Test
    fun observeCategories_emptyCategoriesFromDatabase() = runTest {
        categoryDao.deleteCategories()
        val categories = localSource.observeCategories().first()
        Assert.assertTrue(categories.isEmpty())
    }

    @Test
    fun observeCategories_requestAllCategoriesFromDatabase() = runTest {
        val categories = localSource.observeCategories().first()
        Assert.assertTrue(categories.isNotEmpty())
        Assert.assertEquals(localCategories, categories)
        Assert.assertEquals(localCategories.size, categories.size)
    }

    @Test
    fun getCategoryById_requestDetailCategoryFromDatabase() = runTest {
        val sample = localCategories[0]
        val actual = localSource.getCategoryById(sample.categoryId)
        Assert.assertNotNull(actual)
        Assert.assertEquals(sample.name, actual?.name)
        Assert.assertEquals(sample.type, actual?.type)
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
        Assert.assertNotNull(category)
        Assert.assertEquals(newCategory.name, category?.name)
        Assert.assertEquals(newCategory.type, category?.type)
    }

    @Test
    fun deleteCategory_successDeleteCategoryFromDatabase() = runTest {
        val initial = localSource.observeCategories().first()
        localSource.deleteCategory(initial[0].categoryId)

        val afterDelete = localSource.observeCategories().first()

        Assert.assertEquals(afterDelete.size, initial.size - 1)
    }

    @Test
    fun deleteCategories_removeAllCategoriesFromDatabase() = runTest {
        localSource.deleteCategories()
        val categories = localSource.observeCategories().first()
        Assert.assertTrue(categories.isEmpty())
    }
}