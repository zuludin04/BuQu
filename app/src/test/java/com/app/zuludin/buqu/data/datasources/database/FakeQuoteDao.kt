package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryAndQuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeQuoteDao(initialQuotes: List<QuoteEntity>? = emptyList()) : QuoteDao {
    private var _quotes: MutableMap<String, QuoteEntity>? = null

    var quotes: List<QuoteEntity>?
        get() = _quotes?.values?.toList()
        set(value) {
            _quotes = value?.associateBy { it.quoteId }?.toMutableMap()
        }

    init {
        quotes = initialQuotes
    }

    override fun observeAllQuotes(): Flow<List<CategoryAndQuoteEntity>> {
        val data = quotes?.map { it.toQuoteAndCategory() } ?: throw Exception("Quote is empty")
        return flow { emit(data) }
    }

    override suspend fun getQuoteDetail(quoteId: String): CategoryAndQuoteEntity? {
        val data = _quotes?.get(quoteId)
        return data?.toQuoteAndCategory()
    }

    override suspend fun upsert(quote: QuoteEntity) {
        _quotes?.put(quote.quoteId, quote)
    }

    override suspend fun deleteById(quoteId: String) {
        _quotes?.remove(quoteId)
    }

    override suspend fun deleteQuotes() {
        _quotes?.clear()
    }

    override suspend fun checkQuoteCategoryUsed(categoryId: String): List<QuoteEntity> {
        return quotes ?: throw Exception("Quote is empty")
    }

}

fun QuoteEntity.toQuoteAndCategory(): CategoryAndQuoteEntity {
    return CategoryAndQuoteEntity(
        quoteId = this.quoteId,
        quote = this.quote,
        book = this.book,
        author = this.author,
        page = this.page,
        categoryId = this.categoryId,
        name = "",
        color = "",
        type = ""
    )
}