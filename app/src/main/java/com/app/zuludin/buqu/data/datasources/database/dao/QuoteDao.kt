package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryAndQuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Transaction
    @Query("SELECT * FROM quote INNER JOIN category ON quote.categoryId = category.categoryId")
    fun observeAllQuotes(): Flow<List<CategoryAndQuoteEntity>>

//    @Query("SELECT * FROM quote WHERE quoteId = :quoteId")
//    fun observeById(quoteId: String): Flow<QuoteEntity>
//
//    @Query("SELECT * FROM quote WHERE quoteId = :quoteId")
//    suspend fun getById(quoteId: String): QuoteEntity?

    @Transaction
    @Query("SELECT * FROM quote INNER JOIN category ON quote.categoryId = category.categoryId WHERE quoteId = :quoteId")
    suspend fun getQuoteDetail(quoteId: String): CategoryAndQuoteEntity?

    @Upsert
    suspend fun upsert(quote: QuoteEntity)

    @Query("DELETE FROM quote WHERE quoteId = :quoteId")
    suspend fun deleteById(quoteId: String)
}