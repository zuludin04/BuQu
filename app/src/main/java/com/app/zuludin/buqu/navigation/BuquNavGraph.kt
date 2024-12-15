package com.app.zuludin.buqu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.CATEGORY_TITLE_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.QUOTE_ID_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.TITLE_ARG
import com.app.zuludin.buqu.ui.category.CategorySelectScreen
import com.app.zuludin.buqu.ui.category.upsert.CategoryUpsertScreen
import com.app.zuludin.buqu.ui.quote.HomeScreen
import com.app.zuludin.buqu.ui.settings.SettingsScreen
import com.app.zuludin.buqu.ui.upsertquote.UpsertQuoteScreen

@Composable
fun BuquNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: BuquNavigationActions = remember(navController) {
        BuquNavigationActions(navController)
    }
) {
    NavHost(navController, startDestination = BuquDestinations.QUOTES_ROUTE) {
        composable(BuquDestinations.QUOTES_ROUTE) {
            HomeScreen(
                onQuoteClick = { navActions.navigateToUpsertQuote("Update Quote", it) },
            )
        }
        composable(BuquDestinations.SETTING_ROUTE) {
            SettingsScreen(onOpenCategorySelectScreen = { navActions.navigateToCategorySelect() })
        }

        composable(BuquDestinations.UPSERT_QUOTE_ROUTE, arguments = listOf(
            navArgument(TITLE_ARG) { type = NavType.StringType },
            navArgument(QUOTE_ID_ARG) { type = NavType.StringType; nullable = true }
        )) { entry ->
            val title = entry.arguments?.getString(TITLE_ARG)
            UpsertQuoteScreen(
                onBack = { navController.popBackStack() },
                topAppBarTitle = title ?: ""
            )
        }

        composable(BuquDestinations.CATEGORY_SELECT_ROUTE) {
            CategorySelectScreen(
                onBack = { navController.popBackStack() },
                onUpsertCategory = {
                    navActions.navigateToUpsertCategory("Insert Category")
                }
            )
        }

        composable(
            BuquDestinations.CATEGORY_UPSERT_ROUTE,
            arguments = listOf(
                navArgument(CATEGORY_TITLE_ARG) { type = NavType.StringType }
            )
        ) { entry ->
            val title = entry.arguments?.getString(CATEGORY_TITLE_ARG)
            CategoryUpsertScreen(title = title ?: "", onBack = { navController.popBackStack() })
        }
    }
}

val bottomBarRoutes = setOf(BuquDestinations.QUOTES_ROUTE, BuquDestinations.SETTING_ROUTE)