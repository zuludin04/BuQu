package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.BookDao
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBookDao(initialBooks: List<BookEntity>? = emptyList()) : BookDao {
    private var _books: MutableMap<String, BookEntity>? = null

    var books: List<BookEntity>?
        get() = _books?.values?.toList()
        set(value) {
            _books = value?.associateBy { it.bookId }?.toMutableMap()
        }

    init {
        books = initialBooks
    }

    override fun observeAllBooks(): Flow<List<BookEntity>> {
        return flow { emit(books ?: emptyList()) }
    }

    override suspend fun getById(bookId: String): BookEntity? {
        return _books?.get(bookId)
    }

    override suspend fun upsert(book: BookEntity) {
        _books?.put(book.bookId, book)
    }

    override suspend fun deleteById(bookId: String) {
        _books?.remove(bookId)
    }

    override suspend fun deleteBooks() {
        _books?.clear()
    }
}
