package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(categoryId: String): Category?

    suspend fun upsertCategory(
        categoryId: String?,
        name: String,
        color: String,
        type: String
    )

    suspend fun deleteCategory(categoryId: String)
}