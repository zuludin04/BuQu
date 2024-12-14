package com.app.zuludin.buqu.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.R
import com.app.zuludin.buqu.util.BuQuToolbar

@Composable
fun CategorySelectScreen(onBack: () -> Unit) {
    Scaffold(
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
                onClick = {},
            ) {
                Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                CategoryItem(Color(0xFF9C27B0), "Character")
                Divider()
            }
            item {
                CategoryItem(Color(0xFF3F51B5), "Quote")
                Divider()
            }
            item {
                CategoryItem(Color(0xFF03A9F4), "Motivation")
                Divider()
            }
            item {
                CategoryItem(Color(0xFFFFC107), "Funny")
                Divider()
            }
        }
    }
}

@Composable
private fun CategoryItem(color: Color, category: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(color)
        )
        Text(
            category, modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    CategoryItem(Color(0xFFE91E63), "Character")
}

@Preview
@Composable
private fun CategorySelectScreenPreview() {
    CategorySelectScreen {  }
}