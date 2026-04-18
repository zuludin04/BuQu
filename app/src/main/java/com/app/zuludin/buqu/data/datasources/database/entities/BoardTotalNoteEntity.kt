package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo

data class BoardTotalNoteEntity(
    @ColumnInfo("boardId")
    var boardId: String,

    @ColumnInfo("name")
    var name: String,

    @ColumnInfo("color")
    var color: String,

    @ColumnInfo("totalNote")
    val totalNote: Int
)