package com.app.zuludin.buqu.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.zuludin.buqu.R
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
            if (currentRoute in bottomBarRoutes) {
                FloatingActionButton(
                    modifier = Modifier.testTag("UpsertQuote"),
                    onClick = {
                        navActions.navigateToUpsertQuote("Insert Quote", null)
                    },
                ) {
                    Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, null) },
                        label = { Text("Home") },
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
                        icon = { Icon(Icons.Filled.Settings, null) },
                        label = { Text("Settings") },
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