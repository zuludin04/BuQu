package com.app.zuludin.buqu.domain.usecases

import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import com.app.zuludin.buqu.core.utils.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: ICategoryRepository) {
    operator fun invoke(): Flow<Async<List<Category>>> {
        return repository.getCategories().map { Async.Success(it) }
            .catch { Async.Error("Error Loading Categories") }
    }
}