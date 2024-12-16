package com.app.zuludin.buqu.data.datasources.source

import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteAndCategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteLocalDataSource @Inject constructor(
    private val quoteDao: QuoteDao,
    private val categoryDao: CategoryDao
) {
    fun getQuotes(): Flow<List<QuoteAndCategoryEntity>> = quoteDao.observeAllQuotes()

    fun getQuoteById(quoteId: String): Flow<QuoteEntity> = quoteDao.observeById(quoteId)

    suspend fun getById(quoteId: String): QuoteEntity? = quoteDao.getById(quoteId)

    suspend fun getQuoteDetail(quoteId: String): QuoteAndCategoryEntity? =
        quoteDao.getQuoteDetail(quoteId)

    suspend fun upsertQuote(quote: QuoteEntity) = quoteDao.upsert(quote)

    suspend fun deleteQuote(quoteId: String) = quoteDao.deleteById(quoteId)

    fun getCategories(): Flow<List<CategoryEntity>> = categoryDao.observeAllCategories()

    suspend fun getCategoryById(categoryId: String): CategoryEntity? =
        categoryDao.getById(categoryId)

    suspend fun upsertCategory(category: CategoryEntity) = categoryDao.upsert(category)

    suspend fun deleteCategory(categoryId: String) = categoryDao.deleteById(categoryId)
}