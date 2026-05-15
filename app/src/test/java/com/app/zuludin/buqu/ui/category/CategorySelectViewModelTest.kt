package com.app.zuludin.buqu.ui.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.domain.models.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: CategoryRepository

    private lateinit var viewModel: CategorySelectViewModel

    @Before
    fun setUp() {
        `when`(repository.observeCategories()).thenReturn(flowOf(emptyList()))
        viewModel = CategorySelectViewModel(repository)
    }

    @Test
    fun createCategory_successfullyCreateNewCategory() = runTest {
        val name = "Vision"
        val color = "000000"
        viewModel.upsertCategory(name, color)
        verify(repository).upsertCategory(
            categoryId = null,
            name = name,
            color = color,
            type = "Quote"
        )
    }

    @Test
    fun deleteCategory_successfullyDeleteCategory() = runTest {
        val category = Category("cat1", "Motivation", "000000", "Quote")
        viewModel.selectCategory(category)

        `when`(repository.deleteCategory("cat1")).thenReturn(false) // returns false if NOT in use (successfully deleted)

        viewModel.deleteCategory()
        verify(repository).deleteCategory("cat1")
        assertFalse(viewModel.categoryInUse.value)
    }

    @Test
    fun deleteCategory_failedDeleteCategoryBecauseInUse() = runTest {
        val category = Category("cat1", "Motivation", "000000", "Quote")
        viewModel.selectCategory(category)

        `when`(repository.deleteCategory("cat1")).thenReturn(true) // returns true if IN USE

        viewModel.deleteCategory()
        verify(repository).deleteCategory("cat1")
        assertTrue(viewModel.categoryInUse.value)
    }
}
