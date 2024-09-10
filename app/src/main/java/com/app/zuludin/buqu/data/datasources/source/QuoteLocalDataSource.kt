package com.app.zuludin.buqu.data.datasources.source

import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteLocalDataSource @Inject constructor(private val quoteDao: QuoteDao) {
    fun getQuotes(): Flow<List<QuoteEntity>> = quoteDao.observeAllQuotes()

    fun getQuoteById(quoteId: String): Flow<QuoteEntity> = quoteDao.observeById(quoteId)

    suspend fun upsertQuote(quote: QuoteEntity) = quoteDao.upsert(quote)

    suspend fun deleteQuote(quoteId: String) = quoteDao.deleteById(
        quoteId
    )
}