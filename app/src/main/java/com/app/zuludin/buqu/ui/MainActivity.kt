package com.app.zuludin.buqu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.zuludin.buqu.ui.main.MainScreen
import com.app.zuludin.buqu.util.theme.BuQuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BuQuTheme {
                val navController: NavHostController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}
