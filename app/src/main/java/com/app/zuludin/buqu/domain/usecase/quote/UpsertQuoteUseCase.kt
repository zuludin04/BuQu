package com.app.zuludin.buqu.domain.usecase.quote

import com.app.zuludin.buqu.domain.models.InvalidQuoteException
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class UpsertQuoteUseCase @Inject constructor(private val repository: IQuoteRepository) {
    @Throws(InvalidQuoteException::class)
    suspend operator fun invoke(quoteId: String?, quote: Quote) {
        if (quote.quote.isBlank() && quote.image.isBlank()) {
            throw InvalidQuoteException("Quote content can't be empty")
        }

        repository.upsertQuote(
            quoteId = quoteId,
            quote = quote.quote,
            author = quote.author,
            book = quote.book,
            page = quote.page,
            categoryId = quote.categoryId,
            bookId = quote.bookId,
            image = quote.image
        )
    }
}