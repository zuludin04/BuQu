package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("board")
data class BoardEntity(
    @PrimaryKey
    @ColumnInfo("boardId")
    var boardId: String,

    @ColumnInfo("name")
    var name: String,

    @ColumnInfo("color")
    var color: String
)
