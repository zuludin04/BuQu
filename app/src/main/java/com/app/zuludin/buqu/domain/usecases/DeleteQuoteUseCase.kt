package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class DeleteQuoteUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    suspend operator fun invoke(quoteId: String) {
        quoteRepository.deleteQuote(quoteId)
    }
}