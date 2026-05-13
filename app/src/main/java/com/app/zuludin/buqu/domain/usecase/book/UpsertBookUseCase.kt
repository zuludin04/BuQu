package com.app.zuludin.buqu.domain.usecase.book

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.InvalidBookException
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import javax.inject.Inject

class UpsertBookUseCase @Inject constructor(private val repository: IBookRepository) {
    @Throws(InvalidBookException::class)
    suspend operator fun invoke(bookId: String?, book: Book) {
        if (book.title.isBlank() && book.author.isEmpty()) {
            throw InvalidBookException("Book title and author can't be empty")
        }

        if (book.author.isBlank()) {
            throw InvalidBookException("Book author can't be empty")
        }

        if (book.title.isBlank()) {
            throw InvalidBookException("Book title can't be empty")
        }

        repository.upsertBook(
            bookId = bookId,
            title = book.title,
            author = book.author,
            cover = book.cover,
            description = book.description,
            totalPages = book.totalPages,
            publisher = book.publisher,
            year = book.year
        )
    }
}