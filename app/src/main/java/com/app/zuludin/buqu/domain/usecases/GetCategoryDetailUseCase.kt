package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import javax.inject.Inject

class GetCategoryDetailUseCase @Inject constructor(private val category: ICategoryRepository) {
    suspend operator fun invoke(categoryId: String): Category? =
        category.getCategoryById(categoryId)
}