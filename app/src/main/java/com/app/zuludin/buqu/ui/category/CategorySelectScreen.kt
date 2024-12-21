package com.app.zuludin.buqu.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.ui.quote.TasksEmptyContent
import com.app.zuludin.buqu.util.BuQuToolbar
import com.app.zuludin.buqu.util.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectScreen(
    onBack: () -> Unit,
    viewModel: CategorySelectViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedCategory by remember {
        mutableStateOf(
            Category(
                categoryId = "",
                name = "",
                color = "",
                type = ""
            )
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            BuQuToolbar(
                "Category",
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                    selectedCategory = Category("", "", colors[0], "")
                },
            ) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
            }
        }
    ) { paddingValues ->
        if (uiState.categories.isEmpty()) {
            TasksEmptyContent()
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(uiState.categories) { cat ->
                    CategoryItem(
                        color = Color(android.graphics.Color.parseColor("#${cat.color}")),
                        category = cat,
                        onClick = {
                            showBottomSheet = true
                            selectedCategory = cat
                        },
                    )
                    Divider()
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            CategoryUpsertSheet(
                color = selectedCategory.color,
                name = selectedCategory.name,
                showDeleteButton = selectedCategory.categoryId != "",
                onUpsertCategory = { color, name ->
                    viewModel.upsertCategory(
                        color = color,
                        name = name,
                        id = selectedCategory.categoryId
                    )
                    showBottomSheet = false
                },
                onDeleteCategory = {
                    viewModel.deleteCategory(selectedCategory.categoryId)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
private fun CategoryItem(color: Color, category: Category, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(category.categoryId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(color)
        )
        Text(
            category.name, modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    CategoryItem(
        color = Color(0xFFE91E63),
        category = Category(
            categoryId = "",
            name = "",
            color = "",
            type = ""
        ),
        onClick = {}
    )
}

@Preview
@Composable
private fun CategorySelectScreenPreview() {
    CategorySelectScreen(onBack = {})
}