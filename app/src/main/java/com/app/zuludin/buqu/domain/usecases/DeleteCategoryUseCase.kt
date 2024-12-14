package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val repository: ICategoryRepository) {
    suspend operator fun invoke(categoryId: String) {
        repository.deleteCategory(categoryId)
    }
}