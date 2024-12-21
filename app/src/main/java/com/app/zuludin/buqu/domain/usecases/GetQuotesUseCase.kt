package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.core.utils.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(private val quoteRepository: IQuoteRepository) {
    operator fun invoke(): Flow<Async<List<Quote>>> {
        return quoteRepository.getQuotes().map { Async.Success(it) }
            .catch { Async.Error("Error Loading Quotes") }
    }
}