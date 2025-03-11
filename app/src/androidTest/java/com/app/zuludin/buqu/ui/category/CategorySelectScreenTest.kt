package com.app.zuludin.buqu.ui.category

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
class CategorySelectScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: CategoryRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun displayInitialCategoryState() {
        setContent()

        composeTestRule.onNodeWithText("Motivation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Motivation").assertHasClickAction()

        composeTestRule.onNodeWithText("Character").assertIsDisplayed()
        composeTestRule.onNodeWithText("Character").assertHasClickAction()

        composeTestRule.onNodeWithText("Inspiration").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inspiration").assertHasClickAction()
    }

    @Test
    fun displayNewCategoryItem() = runTest {
        repository.upsertCategory(null, "Anime", "00FF00", "Quote")

        setContent()

        composeTestRule.onNodeWithText("Motivation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Motivation").assertHasClickAction()

        composeTestRule.onNodeWithText("Anime").assertIsDisplayed()
        composeTestRule.onNodeWithText("Anime").assertHasClickAction()
    }

    @Test
    fun displayUpsertCategoryBottomSheet() {
        setContent()

        composeTestRule.onNodeWithText("Motivation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Motivation").assertHasClickAction()

        composeTestRule.onNodeWithTag("UpsertCategory").assertHasClickAction()
        composeTestRule.onNodeWithTag("UpsertCategory").performClick()

        composeTestRule.onNodeWithTag("CategoryBottomSheet").assertIsDisplayed()

        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AddCategory").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AddCategory").assertHasClickAction()
    }

    private fun setContent() {
        composeTestRule.setContent {
            BuQuTheme {
                Surface {
                    CategorySelectScreen(
                        viewModel = CategorySelectViewModel(repository),
                        onBack = {}
                    )
                }
            }
        }
    }
}