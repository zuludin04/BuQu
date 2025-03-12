package com.app.zuludin.buqu.ui.upsertquote

import androidx.compose.material.Surface
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.app.zuludin.buqu.HiltTestActivity
import com.app.zuludin.buqu.core.theme.BuQuTheme
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class UpsertQuoteScreenTest {
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

        composeTestRule.setContent {
            BuQuTheme {
                Surface {
                    UpsertQuoteScreen(
                        viewModel = UpsertQuoteViewModel(
                            quoteRepo, categoryRepo, SavedStateHandle()
                        ),
                        topAppBarTitle = "",
                        onBack = {},
                        onShareQuote = {},
                    )
                }
            }
        }
    }

    @Test
    fun insertQuoteFailed() {
        findTextField("Quote").performTextClearance()
        findTextField("Book").performTextClearance()
        findTextField("Page").performTextClearance()
        findTextField("Author").performTextClearance()

        composeTestRule.onNodeWithTag("AddNewQuote").performClick()

        composeTestRule.onNodeWithText("Make sure to fill all forms").assertIsDisplayed()
    }

    @Test
    fun insertQuoteSuccess() = runTest {
        findTextField("Quote").performTextInput("Hallo")
        findTextField("Book").performTextInput("Asa")
        findTextField("Page").performTextInput("12")
        findTextField("Author").performTextInput("Mobi")

        composeTestRule.onNodeWithTag("AddNewQuote").performClick()

        composeTestRule.onNodeWithText("Make sure to fill all forms").assertIsNotDisplayed()

        val quote = quoteRepo.getQuotes().first()
        assertNotNull(quote)
        assertTrue(quote.isNotEmpty())
        assertEquals("Hallo", quote[0].quote)
        assertEquals("Asa", quote[0].book)
    }

    private fun findTextField(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(text)
        )
    }
}