package com.app.zuludin.buqu.domain.usecase.book

import com.app.zuludin.buqu.domain.models.BookDetailData
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    private val bookRepository: IBookRepository,
    private val quoteRepository: IQuoteRepository,
) {
    suspend operator fun invoke(bookId: String): BookDetailData {
        val quotes = quoteRepository.observeQuotes().first().filter { it.bookId == bookId }
        val book = bookRepository.getBookById(bookId)
        return BookDetailData(book = book, quotes = quotes)
    }
}