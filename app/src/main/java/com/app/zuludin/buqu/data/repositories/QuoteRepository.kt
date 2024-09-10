package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.data.datasources.source.QuoteLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val localSource: QuoteLocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) :
    IQuoteRepository {
    override fun getQuotes(): Flow<List<Quote>> {
        return localSource.getQuotes().map { quotes ->
            withContext(dispatcher) {
                quotes.toExternal()
            }
        }
    }

    override fun getQuoteById(quoteId: String): Flow<Quote> {
        return localSource.getQuoteById(quoteId).map { it.toExternal() }
    }

    override suspend fun upsertQuote(quote: String, author: String) {
        val quoteId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }
        val savedQuote = Quote(quoteId = quoteId, quote = quote, author = author)
        localSource.upsertQuote(savedQuote.toLocal())
    }

    override suspend fun deleteQuote(quoteId: String) {
        localSource.deleteQuote(quoteId)

    }
}