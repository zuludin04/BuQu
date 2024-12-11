package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class UpsertQuoteUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    suspend operator fun invoke(
        quoteId: String?,
        quote: String,
        author: String,
        book: String,
        page: String,
        image: String?
    ) {
        quoteRepository.upsertQuote(quoteId, quote, author, book, page.toInt(), image)
    }
}