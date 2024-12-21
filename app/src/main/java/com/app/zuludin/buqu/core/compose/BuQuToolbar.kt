package com.app.zuludin.buqu.core.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.theme.BuQuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuQuToolbar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    backButton: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        windowInsets = WindowInsets(0, 0, 0, 0),
        navigationIcon = backButton,
        actions = actions,
        modifier = Modifier.fillMaxWidth().shadow(elevation = 2.dp)
    )
}

@Preview
@Composable
private fun ToolbarPreview() {
    BuQuTheme {
        Surface {
            BuQuToolbar(
                title = "BuQu",
                backButton = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, "Back Button")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Menu, "Back Button")
                    }
                },
            )
        }
    }
}