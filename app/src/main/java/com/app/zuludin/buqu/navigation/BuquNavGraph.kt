package com.app.zuludin.buqu.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.zuludin.buqu.ui.quote.HomeScreen

@Composable
fun BuquNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BuquNavigation.QUOTE_NAVIGATION) {
        composable(BuquNavigation.QUOTE_NAVIGATION) {
            HomeScreen()
        }
        composable(BuquNavigation.BOOK_NAVIGATION) {
            Box { Text("Book") }
        }
        composable(BuquNavigation.CATEGORY_NAVIGATION) {
            Box { Text("Category") }
        }
        composable(BuquNavigation.SETTING_NAVIGATION) {
            Box { Text("Setting") }
        }
    }
}