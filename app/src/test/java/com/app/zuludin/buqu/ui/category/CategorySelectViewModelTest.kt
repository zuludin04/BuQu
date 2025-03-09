package com.app.zuludin.buqu.ui.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.domain.models.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CategorySelectViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CategoryRepository

    private lateinit var viewModel: CategorySelectViewModel

    @Before
    fun setUp() {
        viewModel = CategorySelectViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun createCategory_successfullyCreateNewCategory() = runTest {
        val category =
            Category(categoryId = "cat1", name = "Vision", color = "000000", type = "Quote")
        viewModel.upsertCategory(category.categoryId, category.color, category.name)
        verify(repository).upsertCategory(
            category.categoryId,
            category.name,
            category.color,
            category.type
        )
    }

    @Test
    fun deleteCategory_successfullyDeleteCategory() = runTest {
        `when`(repository.deleteCategory("cat1")).thenReturn(true)

        viewModel.deleteCategory("cat1")
        verify(repository).deleteCategory("cat1")
        assertTrue(repository.deleteCategory("cat1"))
    }

    @Test
    fun deleteCategory_failedDeleteCategory() = runTest {
        `when`(repository.deleteCategory("cat1")).thenReturn(false)

        viewModel.deleteCategory("cat1")
        verify(repository).deleteCategory("cat1")
        assertFalse(repository.deleteCategory("cat1"))
    }
}