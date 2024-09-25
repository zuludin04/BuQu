package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class GetQuoteDetailUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    suspend operator fun invoke(quoteId: String): Quote? = quoteRepository.getQuoteById(quoteId)
}