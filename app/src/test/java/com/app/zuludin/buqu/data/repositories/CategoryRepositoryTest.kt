package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.data.datasources.source.QuoteLocalDataSource
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CategoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: QuoteLocalDataSource

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: CategoryRepository

    @Before
    fun setUp() {
        repository = CategoryRepository(localSource, testDispatcher)
    }

    @Test
    fun getCategories_successLoadCategories() = runTest {
        val categories = DataDummy.generateCategoryDummy()
        val data = flow { emit(categories) }
        `when`(localSource.getCategories()).thenReturn(data)

        val actual = repository.getCategories().first()

        assertNotNull(actual)
        assertTrue(actual.isNotEmpty())
        assertEquals(categories.size, actual.size)
    }

    @Test
    fun getCategoryById_successLoadDetailCategory() = runTest {
        val category = DataDummy.generateCategoryDummy()[0]
        `when`(localSource.getCategoryById(category.categoryId)).thenReturn(category)

        val actual = repository.getCategoryById(category.categoryId)

        assertNotNull(actual)
        assertEquals(category.name, actual?.name)
        assertEquals(category.type, actual?.type)
    }
}