package com.app.zuludin.buqu.data.datasources.source

import com.app.zuludin.buqu.data.datasources.database.entities.CategoryAndQuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow

interface IQuoteLocalDataSource {
    fun getQuotes(): Flow<List<CategoryAndQuoteEntity>>

    suspend fun getQuoteDetail(quoteId: String): CategoryAndQuoteEntity?

    suspend fun upsertQuote(quote: QuoteEntity)

    suspend fun deleteQuote(quoteId: String)

    fun getCategories(): Flow<List<CategoryEntity>>

    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    suspend fun upsertCategory(category: CategoryEntity)

    suspend fun deleteCategory(categoryId: String)

    suspend fun deleteCategories()

    suspend fun upsertCategories(categories: List<CategoryEntity>)

    suspend fun deleteQuotes()
}