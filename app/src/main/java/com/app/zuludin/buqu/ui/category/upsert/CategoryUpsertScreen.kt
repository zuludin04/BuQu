package com.app.zuludin.buqu.ui.category.upsert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.ui.upsertquote.TitleInputField
import com.app.zuludin.buqu.util.BuQuToolbar
import com.app.zuludin.buqu.util.colors

@Composable
fun CategoryUpsertScreen(
    title: String,
    onBack: () -> Unit,
    viewModel: CategoryUpsertViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    var cityName: String by remember { mutableStateOf(colors[0]) }
    var expanded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            BuQuToolbar(
                title = title,
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (title == "Update Category") {
                        IconButton(onClick = viewModel::deleteCategory) {
                            Icon(Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                content = { Icon(Icons.Filled.Check, null) },
                onClick = viewModel::saveCategory
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = "Name",
                capitalization = KeyboardCapitalization.Sentences,
                value = uiState.name,
                onChanged = viewModel::updateName,
            )

            Row(
                Modifier
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
            ) {
                Text(text = cityName)
                Icon(imageVector = Icons.Filled.ArrowDropDown, "")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                colors.forEach { city ->
                    val isSelected = city == cityName
                    val style = if (isSelected) {
                        MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.secondary
                        )
                    } else {
                        MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colors.onSurface
                        )
                    }

                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            cityName = city
                            viewModel.updateColor(city)
                        }
                    ) {
                        Text(city, style = style)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isCategorySaved) {
            onBack()
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
        }

        if (uiState.isError) {
            scaffoldState.snackbarHostState.showSnackbar("Make sure to fill all forms")
            viewModel.errorMessageShown()
        }
    }
}