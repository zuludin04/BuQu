package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    operator fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getQuotes()
    }
}