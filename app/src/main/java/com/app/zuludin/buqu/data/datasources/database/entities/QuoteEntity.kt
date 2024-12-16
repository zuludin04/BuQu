package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class QuoteEntity(
    @PrimaryKey
    @ColumnInfo(name = "quoteId")
    var quoteId: String,

    @ColumnInfo(name = "quote")
    var quote: String,

    @ColumnInfo(name = "author")
    var author: String,

    @ColumnInfo(name = "book")
    var book: String,

    @ColumnInfo(name = "page")
    var page: Int,

    @ColumnInfo(name = "categoryId")
    var categoryId: String,
)
