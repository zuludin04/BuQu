package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuoteDetailUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    operator fun invoke(quoteId: String): Flow<Quote> = quoteRepository.getQuoteById(quoteId)
}