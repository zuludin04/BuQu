package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quote")
    fun observeAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quote WHERE quoteId = :quoteId")
    fun observeById(quoteId: String): Flow<QuoteEntity>

    @Upsert
    suspend fun upsert(quote: QuoteEntity)

    @Query("DELETE FROM quote WHERE quoteId = :quoteId")
    suspend fun deleteById(quoteId: String)
}