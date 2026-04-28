package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun observeAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book WHERE bookId = :bookId")
    suspend fun getById(bookId: String): BookEntity?

    @Upsert
    suspend fun upsert(book: BookEntity)

    @Query("DELETE FROM book WHERE bookId = :bookId")
    suspend fun deleteById(bookId: String)

    @Query("DELETE FROM book")
    suspend fun deleteBooks()
}
