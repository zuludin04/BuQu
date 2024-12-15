package com.app.zuludin.buqu.ui.category.upsert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.app.zuludin.buqu.util.BuQuToolbar
import com.app.zuludin.buqu.util.colors

@Composable
fun CategoryUpsertScreen(title: String, onBack: () -> Unit) {
    var cityName: String by remember { mutableStateOf(colors[0]) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BuQuToolbar(
                title = title,
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Back arrow here
            Row(Modifier.clickable { // Anchor view
                expanded = !expanded
            }) { // Anchor view
                Text(text = cityName) // City name label
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
                        }
                    ) {
                        Text(city, style = style)
                    }
                }
            }
        }
    }
}