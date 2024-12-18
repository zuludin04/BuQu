package com.app.zuludin.buqu.core.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.domain.models.Category

@Composable
fun ColorSpinner(
    modifier: Modifier = Modifier,
    currentCategory: Category,
    categories: List<Category>,
    onSelectCategory: (Category) -> Unit,
) {
    var selectedCategory: Category by remember { mutableStateOf(currentCategory) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, SolidColor(Color.Gray)),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp)
                    .background(Color(android.graphics.Color.parseColor("#${selectedCategory.color}")))
            )
            Text(
                text = selectedCategory.name, modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, "")
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedCategory
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
                        selectedCategory = category
                        onSelectCategory(category)
                    }
                ) {
                    Text(category.name, style = style)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ColorSpinnerPreview() {
    ColorSpinner(
        currentCategory = Category("", "", "", ""),
        categories = listOf(),
        onSelectCategory = {}
    )
}