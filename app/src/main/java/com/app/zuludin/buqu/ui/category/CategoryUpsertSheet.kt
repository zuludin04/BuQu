package com.app.zuludin.buqu.ui.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.app.zuludin.buqu.core.colorNames
import com.app.zuludin.buqu.core.colors
import com.app.zuludin.buqu.core.compose.TitleInputField
import com.app.zuludin.buqu.core.icons.PhosphorTrash

@Composable
fun CategoryUpsertSheet(
    color: String,
    name: String,
    showDeleteButton: Boolean,
    onUpsertCategory: (String, String) -> Unit,
    onDeleteCategory: () -> Unit
) {
    var categoryName by remember { mutableStateOf(name) }
    var categoryColor by remember { mutableStateOf(color) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .testTag("CategoryBottomSheet")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(16.dp)
                    .clickable { expanded = true }
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .background(Color("#${categoryColor}".toColorInt()))
                )
            }
            TitleInputField(
                modifier = Modifier.weight(1f),
                singleLine = false,
                label = "Name",
                capitalization = KeyboardCapitalization.Sentences,
                value = categoryName,
                onChanged = { categoryName = it },
            )
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            colors.forEachIndexed { index, color ->
                val isSelected = color == categoryColor
                val style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.5f
                    )
                )

                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        categoryColor = color
                    }
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color("#${color}".toColorInt()))
                        )
                        Box(modifier = Modifier.width(8.dp))
                        Text(colorNames[index], style = style)
                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .testTag("AddCategory"),
                onClick = { onUpsertCategory(categoryColor, categoryName) },
                content = {
                    Text(
                        text = "Save",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
            if (showDeleteButton) {
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.errorContainer),
                    content = {
                        Icon(
                            imageVector = PhosphorTrash,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    },
                    onClick = onDeleteCategory
                )
            }
        }
    }
}

@Preview
@Composable
fun CategoryUpsertSheetPreview() {
    CategoryUpsertSheet(
        "000000",
        "Hallo",
        true,
        { _, _ -> },
        {}
    )
}