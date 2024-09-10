package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Quote
import kotlinx.coroutines.flow.Flow

interface IQuoteRepository {
    fun getQuotes(): Flow<List<Quote>>

    fun getQuoteById(quoteId: String): Flow<Quote>

    suspend fun upsertQuote(quote: String, author: String)

    suspend fun deleteQuote(quoteId: String)
}