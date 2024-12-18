package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo

data class CategoryAndQuoteEntity(
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

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "color")
    var color: String,

    @ColumnInfo(name = "type")
    var type: String
)
