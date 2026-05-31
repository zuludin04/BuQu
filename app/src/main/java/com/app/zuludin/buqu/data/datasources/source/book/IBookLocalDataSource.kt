package com.app.zuludin.buqu.data.datasources.source.book

import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow

interface IBookLocalDataSource {
    fun getBooks(): Flow<List<BookEntity>>
    suspend fun getBookById(bookId: String): BookEntity?
    fun observeSavedBook(bookId: String): Flow<Boolean>
    suspend fun upsertBook(book: BookEntity)
    suspend fun deleteBook(bookId: String)
    suspend fun deleteBooks()
}
