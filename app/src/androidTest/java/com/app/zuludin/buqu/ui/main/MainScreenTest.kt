package com.app.zuludin.buqu.ui.main

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
@LargeTest
@HiltAndroidTest
class MainScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var quoteRepo: QuoteRepository

    @Inject
    lateinit var categoryRepo: CategoryRepository

    private lateinit var navController: TestNavHostController

    @Before
    fun init() {
        hiltRule.inject()

        composeTestRule.setContent {
            BuQuTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MainScreen(navController = navController)
            }
        }
    }

    @Test
    fun changeScreenBetweenNavigation() = runTest {
        // check home screen initial state
        composeTestRule.onNodeWithText("Save the Greatest Quote").assertIsDisplayed()

        // go to settings nav
        composeTestRule.onNodeWithText("Settings").performClick()

        // check setting menu
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rate").assertIsDisplayed()

        // go back to home nav
        composeTestRule.onNodeWithText("Home").performClick()
    }

    @Test
    fun goToUpsertScreenFromHomePage() {
        // check home screen initial state
        composeTestRule.onNodeWithText("Save the Greatest Quote").assertIsDisplayed()

        // click FAB upsert
        composeTestRule.onNodeWithTag("UpsertQuote").assertHasClickAction()
        composeTestRule.onNodeWithTag("UpsertQuote").performClick()

        // check upsert screen is display
        composeTestRule.onNodeWithText("Quote").assertIsDisplayed()
        composeTestRule.onNodeWithText("Book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Page").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author").assertIsDisplayed()
    }
}