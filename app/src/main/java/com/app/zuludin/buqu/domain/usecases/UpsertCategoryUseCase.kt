package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import javax.inject.Inject

class UpsertCategoryUseCase @Inject constructor(private val repository: ICategoryRepository) {
    suspend operator fun invoke(
        categoryId: String?,
        name: String,
        color: Int,
        type: String
    ) {
        repository.upsertCategory(categoryId, name, color, type)
    }
}