package com.app.zuludin.buqu.ui.quote

import androidx.compose.material.Surface
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.app.zuludin.buqu.HiltTestActivity
import com.app.zuludin.buqu.core.theme.BuQuTheme
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var quoteRepo: QuoteRepository

    @Inject
    lateinit var categoryRepo: CategoryRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun showInitialEmptyState() = runTest {
        setContent()

        composeTestRule.onNodeWithText("Save the Greatest Quote").assertIsDisplayed()
    }

    @Test
    fun showQuoteFromDatabase() = runTest {
        // add new quote
        addNewQuote()

        setContent()

        composeTestRule.onNodeWithText("Fida").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dsa").assertIsDisplayed()
    }

    @Test
    fun checkQuoteItemIsClickable() = runTest {
        addNewQuote()

        setContent()

        composeTestRule.onNodeWithText("Fida").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dsa").assertIsDisplayed()

        composeTestRule.onNodeWithTag("QuoteItem").assertIsDisplayed()
        composeTestRule.onNodeWithTag("QuoteItem").assertHasClickAction()
    }

    private fun setContent() {
        composeTestRule.setContent {
            BuQuTheme {
                Surface {
                    HomeScreen(
                        viewModel = QuoteViewModel(quoteRepo, categoryRepo),
                        onQuoteClick = {}
                    )
                }
            }
        }
    }

    private suspend fun addNewQuote() {
        quoteRepo.upsertQuote(null, "Fida", "Sa", "Dsa", 12, "a76c5015-34c7-4a54-bdfb-c5ed2010b7c9")
    }
}