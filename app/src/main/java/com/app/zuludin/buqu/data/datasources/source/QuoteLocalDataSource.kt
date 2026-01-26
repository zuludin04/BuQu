package com.app.zuludin.buqu.data.datasources.source

import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryAndQuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteLocalDataSource @Inject constructor(
    private val quoteDao: QuoteDao,
    private val categoryDao: CategoryDao
) : IQuoteLocalDataSource {

    override fun getQuotes(): Flow<List<CategoryAndQuoteEntity>> = quoteDao.observeAllQuotes()

    override suspend fun getQuotesByCategory(categoryId: String): List<CategoryAndQuoteEntity> =
        quoteDao.quoteByCategory(categoryId)

    override suspend fun getQuoteDetail(quoteId: String): CategoryAndQuoteEntity? =
        quoteDao.getQuoteDetail(quoteId)

    override suspend fun upsertQuote(quote: QuoteEntity) = quoteDao.upsert(quote)

    override suspend fun deleteQuote(quoteId: String) = quoteDao.deleteById(quoteId)

    override fun getCategories(): Flow<List<CategoryEntity>> = categoryDao.observeAllCategories()

    override suspend fun getCategoryById(categoryId: String): CategoryEntity? =
        categoryDao.getById(categoryId)

    override suspend fun upsertCategory(category: CategoryEntity) = categoryDao.upsert(category)

    override suspend fun deleteCategory(categoryId: String) = categoryDao.deleteById(categoryId)

    override suspend fun deleteCategories() = categoryDao.deleteCategories()

    override suspend fun upsertCategories(categories: List<CategoryEntity>) =
        categoryDao.upsertCategories(categories)

    override suspend fun deleteQuotes() = quoteDao.deleteQuotes()

    override suspend fun checkCategoryUsed(categoryId: String) =
        quoteDao.checkQuoteCategoryUsed(categoryId)
}