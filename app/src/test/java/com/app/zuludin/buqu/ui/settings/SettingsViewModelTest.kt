package com.app.zuludin.buqu.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.MainDispatcherRule
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var quoteRepo: QuoteRepository

    @Mock
    private lateinit var categoryRepo: CategoryRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(quoteRepo, categoryRepo)
    }

    @Test
    fun resetData_deleteAllQuoteAndCategoryData() = runTest {
        viewModel.resetData()

        val state = viewModel.uiState.first()

        verify(quoteRepo).deleteAllQuote()
        verify(categoryRepo).deleteAllCategory()
        assertTrue(state.isResetSuccess)
    }
}