package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Book
import kotlinx.coroutines.flow.Flow

interface IBookRepository {
    fun observeBooks(): Flow<List<Book>>

    suspend fun getBookById(bookId: String): Book?

    suspend fun upsertBook(
        bookId: String?,
        title: String,
        author: String,
        cover: String,
        description: String,
        totalPages: Int,
        publisher: String,
        year: Int
    )

    suspend fun deleteBook(bookId: String)

    suspend fun searchBooks(query: String): List<Book>
}
