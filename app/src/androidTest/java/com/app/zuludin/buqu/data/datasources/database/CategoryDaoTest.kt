package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CategoryDaoTest {
    private lateinit var database: BuQuDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BuQuDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertCategory_success() = runTest {
        val category = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(category)

        val actual = database.categoryDao().getById(category.categoryId)
        assertNotNull(actual)
        assertEquals(category.categoryId, actual?.categoryId)
        assertEquals(category.name, actual?.name)
        assertEquals(category.type, actual?.type)
    }

    @Test
    fun deleteCategory_success() = runTest {
        val category = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(category)

        database.categoryDao().deleteById(category.categoryId)

        val actual = database.categoryDao().observeAllCategories().first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun updateCategory_success() = runTest {
        val category = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(category)

        val newCategory = CategoryEntity(category.categoryId, "Character", "000FFF", "Book")
        database.categoryDao().upsert(newCategory)

        val actual = database.categoryDao().getById(category.categoryId)
        assertNotNull(actual)
        assertEquals(category.categoryId, actual?.categoryId)
        assertEquals(newCategory.name, actual?.name)
        assertEquals(newCategory.type, actual?.type)
    }
}