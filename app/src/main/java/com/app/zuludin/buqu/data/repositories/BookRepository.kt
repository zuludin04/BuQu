package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.data.datasources.network.GoogleBooksApiService
import com.app.zuludin.buqu.data.datasources.source.book.BookLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val localSource: BookLocalDataSource,
    private val apiService: GoogleBooksApiService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : IBookRepository {
    override fun observeBooks(): Flow<List<Book>> {
        return localSource.getBooks().map { books ->
            withContext(dispatcher) {
                books.toExternal()
            }
        }
    }

    override suspend fun getBookById(bookId: String): Book? {
        val localBook = localSource.getBookById(bookId)?.toExternal()
        return localBook ?: getDetailBook(bookId)
    }

    override suspend fun upsertBook(
        bookId: String?,
        title: String,
        author: String,
        cover: String,
        description: String,
        totalPages: Int,
        publisher: String,
        year: Int
    ) {
        if (bookId != null) {
            val book = Book(
                bookId = bookId,
                title = title,
                author = author,
                cover = cover,
                description = description,
                totalPages = totalPages,
                publisher = publisher,
                year = year
            )
            localSource.upsertBook(book.toLocal())
        } else {
            val id = withContext(dispatcher) {
                UUID.randomUUID().toString()
            }
            val book = Book(
                bookId = id,
                title = title,
                author = author,
                cover = cover,
                description = description,
                totalPages = totalPages,
                publisher = publisher,
                year = year
            )
            localSource.upsertBook(book.toLocal())
        }
    }

    override suspend fun deleteBook(bookId: String) {
        localSource.deleteBook(bookId)
    }

    override suspend fun searchBooks(query: String): List<Book> = withContext(dispatcher) {
        try {
            val response = apiService.searchBooks(query, BuildConfig.GOOGLE_BOOKS_API_KEY)
            response.items?.map { item ->
                val year = item.volumeInfo.publishedDate
                    ?.take(4)
                    ?.toIntOrNull()
                    ?: 0
                Book(
                    bookId = item.id,
                    title = item.volumeInfo.title,
                    author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
                    cover = item.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:") ?: "",
                    description = item.volumeInfo.description ?: "",
                    totalPages = item.volumeInfo.pageCount ?: 0,
                    publisher = item.volumeInfo.publisher ?: "",
                    year = year
                )
            } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun getDetailBook(bookId: String): Book? = withContext(dispatcher) {
        try {
            val response = apiService.getBookDetail(bookId, BuildConfig.GOOGLE_BOOKS_API_KEY)
            val year = response.volumeInfo?.publishedDate
                ?.take(4)
                ?.toIntOrNull()
                ?: 0
            val book = Book(
                bookId = response.id ?: "",
                title = response.volumeInfo?.title ?: "",
                author = response.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author",
                cover = response.volumeInfo?.imageLinks?.thumbnail?.replace("http:", "https:")
                    ?: "",
                description = response.volumeInfo?.description ?: "",
                totalPages = response.volumeInfo?.pageCount ?: 0,
                publisher = response.volumeInfo?.publisher ?: "",
                year = year,
            )
            book
        } catch (_: Exception) {
            null
        }
    }
}
