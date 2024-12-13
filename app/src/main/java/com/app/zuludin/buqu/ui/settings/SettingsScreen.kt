package com.app.zuludin.buqu.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            Text("Settings")
        }
    }
}