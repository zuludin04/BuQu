package com.app.zuludin.buqu.data.repositories

import android.util.Log
import com.app.zuludin.buqu.data.datasources.source.QuoteLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val localSource: QuoteLocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ICategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return localSource.getCategories().map { categories ->
            withContext(dispatcher) {
                categories.toExternal()
            }
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        return localSource.getCategoryById(categoryId)?.toExternal()
    }

    override suspend fun upsertCategory(categoryId: String?, name: String, color: String, type: String) {
        if (categoryId != null) {
            val q = getCategoryById(categoryId)!!.copy(
                name = name,
                color = color,
                type = type
            )
            localSource.upsertCategory(q.toLocal())
        } else {
            val id = withContext(dispatcher) {
                UUID.randomUUID().toString()
            }
            val savedCategory =
                Category(
                    categoryId = id,
                    name = name,
                    color = color,
                    type = type
                )
            localSource.upsertCategory(savedCategory.toLocal())
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        localSource.deleteCategory(categoryId)
    }

}