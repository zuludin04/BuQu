package com.app.zuludin.buqu.data.datasources.source.book

import com.app.zuludin.buqu.data.datasources.database.dao.BookDao
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookLocalDataSource @Inject constructor(
    private val bookDao: BookDao
) : IBookLocalDataSource {
    override fun getBooks(): Flow<List<BookEntity>> = bookDao.observeAllBooks()

    override suspend fun getBookById(bookId: String): BookEntity? = bookDao.getById(bookId)

    override suspend fun upsertBook(book: BookEntity) = bookDao.upsert(book)

    override suspend fun deleteBook(bookId: String) = bookDao.deleteById(bookId)

    override suspend fun deleteBooks() = bookDao.deleteBooks()
}
