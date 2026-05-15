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
            BuQuTheme(dynamicColor = false) {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MainScreen(navController = navController)
            }
        }
    }

    @Test
    fun changeScreenBetweenNavigation() = runTest {
        // check quote navigation initial state
        composeTestRule.onNodeWithText("No Quotes Found").assertIsDisplayed()

        // go to books nav
        composeTestRule.onNodeWithText("Books").performClick()
        composeTestRule.onNodeWithText("Your books will appear here").assertIsDisplayed()
        composeTestRule.onNodeWithText("Online").performClick()
        composeTestRule.onNodeWithText("Search online").assertIsDisplayed()

        // go to board nav
        composeTestRule.onNodeWithText("Board").performClick()
        composeTestRule.onNodeWithText("Create Your First Board").assertIsDisplayed()

        // go to settings nav
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rate").assertIsDisplayed()

        // go back to quote nav
        composeTestRule.onNodeWithText("Quotes").performClick()
    }

    @Test
    fun goToUpsertQuoteFromMain() {
        // check home screen initial state
        composeTestRule.onNodeWithText("No Quotes Found").assertIsDisplayed()

        // click FAB upsert
        composeTestRule.onNodeWithTag("AddButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("AddButton").performClick()

        // check upsert quote screen is display
        composeTestRule.onNodeWithTag("QuoteField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("BookField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("CategoryField").assertIsDisplayed()
    }

    @Test
    fun goToUpsertBookFromMain() {
        // check home screen initial state
        composeTestRule.onNodeWithText("No Quotes Found").assertIsDisplayed()

        // go to book nav and click FAB then select input manual
        composeTestRule.onNodeWithText("Books").performClick()
        composeTestRule.onNodeWithTag("AddButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("AddButton").performClick()
        composeTestRule.onNodeWithText("Manual Input").assertHasClickAction()
        composeTestRule.onNodeWithText("Manual Input").performClick()

        // check upsert quote screen is display
        composeTestRule.onNodeWithText("Book Title *").assertIsDisplayed()
        composeTestRule.onNodeWithText("Author *").assertIsDisplayed()
    }

    @Test
    fun goToUpsertBoardFromMain() {
        // check home screen initial state
        composeTestRule.onNodeWithText("No Quotes Found").assertIsDisplayed()

        // click FAB upsert
        composeTestRule.onNodeWithText("Board").performClick()
        composeTestRule.onNodeWithTag("AddButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("AddButton").performClick()

        // check board tools is displayed
        composeTestRule.onNodeWithTag("BoardTools").assertIsDisplayed()
    }

    @Test
    fun goToCategoryFromMain() {
        // check home screen initial state
        composeTestRule.onNodeWithText("No Quotes Found").assertIsDisplayed()

        // check and click category menu
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertHasClickAction()
        composeTestRule.onNodeWithText("Category").performClick()
    }
}