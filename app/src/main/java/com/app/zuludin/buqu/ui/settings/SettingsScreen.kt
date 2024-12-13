package com.app.zuludin.buqu.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.util.BuQuToolbar

/**
 * General
 *  Category
 *  Theme
 *  Reset
 * Other
 *  About
 *  Rate
 * */

@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = { BuQuToolbar("Settings") },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SettingsGroup(name = "General") {
                SettingsClickableComp(
                    name = "Category",
                    icon = R.drawable.ic_category,
                    iconDesc = "",
                    onClick = { },
                )
                SettingsSwitchComp(
                    name = "Theme",
                    icon = R.drawable.ic_theme,
                    iconDesc = "",
//                    state =
                ) {

                }
                SettingsClickableComp(
                    name = "Reset",
                    icon = R.drawable.ic_reset,
                    iconDesc = "",
                    onClick = { },
                )
            }

            SettingsGroup(name = "Other") {
                SettingsClickableComp(
                    name = "About",
                    icon = R.drawable.ic_about,
                    iconDesc = "",
                    onClick = { },
                )
                SettingsClickableComp(
                    name = "Rate",
                    icon = R.drawable.ic_rate,
                    iconDesc = "",
                    onClick = { },
                )
            }
        }
    }
}