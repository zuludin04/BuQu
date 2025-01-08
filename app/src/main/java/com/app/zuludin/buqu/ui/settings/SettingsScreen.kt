package com.app.zuludin.buqu.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.core.compose.BuQuToolbar

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
fun SettingsScreen(
    onOpenCategorySelectScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openConfirmDialog = remember { mutableStateOf(false) }

    Scaffold(topBar = { BuQuToolbar("Settings") }) { paddingValues ->
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
                    onClick = onOpenCategorySelectScreen,
                )
//                SettingsSwitchComp(
//                    name = "Theme",
//                    icon = R.drawable.ic_theme,
//                    iconDesc = "",
////                    state =
//                ) {
//
//                }
                SettingsClickableComp(
                    name = "Reset",
                    icon = R.drawable.ic_reset,
                    iconDesc = "",
                    onClick = { openConfirmDialog.value = true },
                )
            }

            SettingsGroup(name = "Other") {
//                SettingsClickableComp(
//                    name = "About",
//                    icon = R.drawable.ic_about,
//                    iconDesc = "",
//                    onClick = { },
//                )
                SettingsClickableComp(
                    name = "Rate",
                    icon = R.drawable.ic_rate,
                    iconDesc = "",
                    onClick = { },
                )
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isResetSuccess) {
            openConfirmDialog.value = false
            viewModel.successMessageShown()
        }
    }

    if (openConfirmDialog.value) {
        ResetDialogConfirmation(
            onDismissRequest = { openConfirmDialog.value = false },
            onConfirmation = viewModel::resetData
        )
    }
}

@Composable
private fun ResetDialogConfirmation(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .defaultMinSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.defaultMinSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "All data will be deleted. Are you sure?",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("No")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}