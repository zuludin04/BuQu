package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class UpsertQuoteUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    suspend operator fun invoke(quote: String, author: String) {
        quoteRepository.upsertQuote(quote, author)
    }
}