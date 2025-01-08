package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import javax.inject.Inject

class ResetDatabaseUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke() {
        quoteRepository.deleteAllQuote()
        categoryRepository.deleteAllCategory()
    }
}