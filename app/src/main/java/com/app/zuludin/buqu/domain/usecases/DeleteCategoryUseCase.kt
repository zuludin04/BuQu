package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: ICategoryRepository,
    private val quoteRepository: IQuoteRepository
) {
    suspend operator fun invoke(categoryId: String): Boolean {
        val check = quoteRepository.checkCategoryUsed(categoryId)
        if (check) {
            return true
        } else {
            repository.deleteCategory(categoryId)
            return false
        }
    }
}