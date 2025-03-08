package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCategoryDao(initialCategories: List<CategoryEntity>? = emptyList()) : CategoryDao {
    private var _categories: MutableMap<String, CategoryEntity>? = null

    var categories: List<CategoryEntity>?
        get() = _categories?.values?.toList()
        set(value) {
            _categories = value?.associateBy { it.categoryId }?.toMutableMap()
        }

    init {
        categories = initialCategories
    }

    override fun observeAllCategories(): Flow<List<CategoryEntity>> {
        val data = categories ?: throw Exception("Category is empty")
        return flow { emit(data) }
    }

    override fun observeById(categoryId: String): Flow<CategoryEntity> {
        val data = _categories?.get(categoryId) ?: throw Exception("Category not found")
        return flow { emit(data) }
    }

    override suspend fun getById(categoryId: String): CategoryEntity? = _categories?.get(categoryId)

    override suspend fun upsert(quote: CategoryEntity) {
        _categories?.put(quote.categoryId, quote)
    }

    override suspend fun deleteById(categoryId: String) {
        _categories?.remove(categoryId)
    }

    override suspend fun deleteCategories() {
        _categories?.clear()
    }

    override suspend fun upsertCategories(categories: List<CategoryEntity>) {
        categories.forEach {
            _categories?.put(it.categoryId, it)
        }
    }

}