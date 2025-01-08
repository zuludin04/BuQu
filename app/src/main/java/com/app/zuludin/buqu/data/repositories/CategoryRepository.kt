package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
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

    override suspend fun upsertCategory(
        categoryId: String?,
        name: String,
        color: String,
        type: String
    ) {
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

    override suspend fun deleteAllCategory() {
        localSource.deleteCategories()
        val cat1 = CategoryEntity(
            categoryId = "a76c5015-34c7-4a54-bdfb-c5ed2010b7c9",
            name = "Motivation",
            color = "03A9F4",
            type = "Quote"
        )
        val cat2 = CategoryEntity(
            categoryId = "a7fbe08b-74d1-4158-8dc4-5631ad102794",
            name = "Character",
            color = "F44336",
            type = "Quote"
        )
        val cat3 = CategoryEntity(
            categoryId = "ca7fa67a-f11f-42f1-90fc-597924679e77",
            name = "Inspiration",
            color = "CDDC39",
            type = "Quote"
        )
        val cat4 = CategoryEntity(
            categoryId = "a27e43a0-c21e-475a-8ba4-2df060379591",
            name = "Funny",
            color = "009688",
            type = "Quote"
        )
        val cats = listOf(cat1, cat2, cat3, cat4)
        localSource.upsertCategories(cats)
    }
}