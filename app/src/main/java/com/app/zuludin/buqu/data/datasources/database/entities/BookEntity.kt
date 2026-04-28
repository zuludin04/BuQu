package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = "bookId")
    var bookId: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "author")
    var author: String,

    @ColumnInfo(name = "cover")
    var cover: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "totalPages")
    var totalPages: Int
)
