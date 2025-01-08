package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun observeAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun observeById(categoryId: String): Flow<CategoryEntity>

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    suspend fun getById(categoryId: String): CategoryEntity?

    @Upsert
    suspend fun upsert(quote: CategoryEntity)

    @Query("DELETE FROM category WHERE categoryId = :categoryId")
    suspend fun deleteById(categoryId: String)

    @Query("DELETE FROM category")
    suspend fun deleteCategories()

    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)
}