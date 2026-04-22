package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("note_card")
data class NoteCardEntity(
    @PrimaryKey
    @ColumnInfo("noteId")
    var noteId: String,

    @ColumnInfo("title")
    var title: String,

    @ColumnInfo("posX")
    var posX: Float,

    @ColumnInfo("posY")
    var posY: Float,

    @ColumnInfo("color")
    var color: String,

    @ColumnInfo("width")
    var width: Int,

    @ColumnInfo("height")
    var height: Int,

    @ColumnInfo("boardId")
    var boardId: String,

    @ColumnInfo("image")
    var image: String
)
