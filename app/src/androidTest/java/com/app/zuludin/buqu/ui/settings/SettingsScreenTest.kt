package com.app.zuludin.buqu.ui.settings

import androidx.compose.material.Surface
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.app.zuludin.buqu.HiltTestActivity
import com.app.zuludin.buqu.core.theme.BuQuTheme
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class SettingsScreenTest {
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
    fun checkSettingsMenuItem() {
        composeTestRule.setContent {
            BuQuTheme {
                Surface {
                    SettingsScreen(
                        viewModel = SettingsViewModel(quoteRepo, categoryRepo),
                        onOpenCategorySelectScreen = { }
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("General").assertIsDisplayed()

        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertHasClickAction()

        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertHasClickAction()

        composeTestRule.onNodeWithText("Other").assertIsDisplayed()

        composeTestRule.onNodeWithText("Rate").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rate").assertHasClickAction()
    }

    @Test
    fun showResetDialogConfirmation() {
        composeTestRule.setContent {
            BuQuTheme {
                Surface {
                    SettingsScreen(
                        viewModel = SettingsViewModel(quoteRepo, categoryRepo),
                        onOpenCategorySelectScreen = { }
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertHasClickAction()
        composeTestRule.onNodeWithText("Reset").performClick()

        composeTestRule.onNodeWithTag("ResetConfirm").assertIsDisplayed()

        composeTestRule.onNodeWithText("All data will be deleted. Are you sure?")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Yes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Yes").assertHasClickAction()
        composeTestRule.onNodeWithText("No").assertIsDisplayed()
        composeTestRule.onNodeWithText("No").assertHasClickAction()
    }
}