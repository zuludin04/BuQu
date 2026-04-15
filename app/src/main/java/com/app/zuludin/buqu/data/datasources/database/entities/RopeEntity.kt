package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rope")
data class RopeEntity(
    @PrimaryKey
    @ColumnInfo("ropeId")
    var ropeId: String,

    @ColumnInfo("sourceNoteId")
    var sourceNoteId: String,

    @ColumnInfo("targetNoteId")
    var targetNoteId: String,

    @ColumnInfo("boardId")
    var boardId: String
)
