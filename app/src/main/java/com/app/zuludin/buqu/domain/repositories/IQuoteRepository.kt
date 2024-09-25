package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Quote
import kotlinx.coroutines.flow.Flow

interface IQuoteRepository {
    fun getQuotes(): Flow<List<Quote>>

    suspend fun getQuoteById(quoteId: String): Quote?

    suspend fun upsertQuote(
        quoteId: String?,
        quote: String,
        author: String,
        book: String,
        page: Int
    )

    suspend fun deleteQuote(quoteId: String)
}