package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    var categoryId: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "color")
    var color: String,

    @ColumnInfo(name = "type")
    var type: String
)
