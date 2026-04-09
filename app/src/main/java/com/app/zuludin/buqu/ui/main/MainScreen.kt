package com.app.zuludin.buqu.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.icons.PhosphorChalkboardSimple
import com.app.zuludin.buqu.core.icons.PhosphorGear
import com.app.zuludin.buqu.core.icons.PhosphorNote
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.navigation.BuquDestinations
import com.app.zuludin.buqu.navigation.BuquNavGraph
import com.app.zuludin.buqu.navigation.BuquNavigationActions
import com.app.zuludin.buqu.navigation.bottomBarRoutes

@Composable
fun MainScreen(
    navController: NavHostController,
    navActions: BuquNavigationActions = remember(navController) {
        BuquNavigationActions(navController)
    }
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        floatingActionButton = {
            if (currentRoute in bottomBarRoutes && currentRoute != BuquDestinations.SETTING_ROUTE) {
                FloatingActionButton(
                    modifier = Modifier.testTag("UpsertQuote"),
                    onClick = {
                        when (currentRoute) {
                            "quotes" -> navActions.navigateToUpsertQuote("Insert Quote", null)
                            "board" -> navActions.navigateToBoardEditor()
                        }
                    },
                ) {
                    Icon(PhosphorPlus, null)
                }
            }
        },
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(PhosphorNote, null) },
                        label = { Text(stringResource(R.string.quote)) },
                        selected = currentRoute == BuquDestinations.QUOTES_ROUTE,
                        onClick = {
                            if (currentRoute != BuquDestinations.QUOTES_ROUTE) {
                                navController.navigate(BuquDestinations.QUOTES_ROUTE) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(PhosphorChalkboardSimple, null) },
                        label = { Text(stringResource(R.string.board)) },
                        selected = currentRoute == BuquDestinations.BOARD_ROUTE,
                        onClick = {
                            if (currentRoute != BuquDestinations.BOARD_ROUTE) {
                                navController.navigate(BuquDestinations.BOARD_ROUTE) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(PhosphorGear, null) },
                        label = { Text(stringResource(R.string.settings)) },
                        selected = currentRoute == BuquDestinations.SETTING_ROUTE,
                        onClick = {
                            if (currentRoute != BuquDestinations.SETTING_ROUTE) {
                                navController.navigate(BuquDestinations.SETTING_ROUTE) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        BuquNavGraph(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}