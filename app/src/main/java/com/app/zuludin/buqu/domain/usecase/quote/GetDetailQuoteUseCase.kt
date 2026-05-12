package com.app.zuludin.buqu.domain.usecase.quote

import com.app.zuludin.buqu.domain.models.QuoteUpsertData
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetDetailQuoteUseCase @Inject constructor(
    private val quoteRepository: IQuoteRepository,
    private val bookRepository: IBookRepository,
    private val categoryRepository: ICategoryRepository
) {
    suspend operator fun invoke(quoteId: String?): QuoteUpsertData {
        val books = bookRepository.observeBooks()
        val categories = categoryRepository.observeCategories()

        if (quoteId != null) {
            val quote = quoteRepository.getQuoteById(quoteId)
            return QuoteUpsertData(
                quote = quote,
                books = books.first(),
                categories = categories.first()
            )
        } else {
            return QuoteUpsertData(
                quote = null,
                books = books.first(),
                categories = categories.first()
            )
        }
    }
}