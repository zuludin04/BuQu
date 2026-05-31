package com.app.zuludin.buqu.domain.usecase.book

import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.ui.book.detail.BookDetailState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    private val bookRepository: IBookRepository,
    private val quoteRepository: IQuoteRepository,
) {
    operator fun invoke(bookId: String): Flow<BookDetailState> {
        val book = flow { emit(bookRepository.getBookById(bookId)) }
        return combine(
            quoteRepository.observeQuotes(),
            book,
            bookRepository.observeSavedBook(bookId)
        ) { quotes, book, fromDatabase ->
            BookDetailState(
                book = book,
                quotes = quotes.filter { it.bookId == bookId },
                isLoading = false,
                fromDatabase = fromDatabase
            )
        }
    }
}