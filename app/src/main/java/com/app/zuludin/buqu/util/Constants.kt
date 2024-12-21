package com.app.zuludin.buqu.util

import com.app.zuludin.buqu.domain.models.Category

val colors = mutableListOf(
    "F44336", // red
    "E91E63", // pink
    "9C27B0", // purple
    "673AB7", // deep purple
    "3F51B5", // indigo
    "2196F3", // blue
    "03A9F4", // light blue
    "00BCD4", // cyan
    "009688", // teal
    "4CAF50", // green
    "8BC34A", // light green
    "CDDC39", // lime
    "FFEB3B", // yellow
    "FFC107", // amber
    "FF9800", // orange
    "FF5722", // deep orange
    "795548", // brown
    "9E9E9E", // grey
    "607D8B", // blue grey
)

val colorNames = mutableListOf(
    "Red",
    "Pink",
    "Purple",
    "Deep Purple",
    "Indigo",
    "Blue",
    "Light Blue",
    "Cyan",
    "Teal",
    "Green",
    "Light Green",
    "Lime",
    "Yellow",
    "Amber",
    "Orange",
    "Deep Orange",
    "Brown",
    "Grey",
    "Blue Grey"
)

fun categoryColors(): List<Category> {
    val categories = mutableListOf<Category>()

    colors.forEachIndexed { index, s ->
        categories.add(
            Category(
                categoryId = "$index",
                name = colorNames[index],
                color = s,
                type = ""
            )
        )
    }

    return categories
}