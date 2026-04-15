package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.ColumnInfo

data class ConnectedRopeEntity(
    @ColumnInfo("ropeId")
    var ropeId: String,

    @ColumnInfo("sourceNoteId")
    var sourceNoteId: String,

    @ColumnInfo("targetNoteId")
    var targetNoteId: String,

    @ColumnInfo("boardId")
    var boardId: String,

    @ColumnInfo("sourceX")
    var sourceX: Float,

    @ColumnInfo("sourceY")
    var sourceY: Float,

    @ColumnInfo("targetX")
    var targetX: Float,

    @ColumnInfo("targetY")
    var targetY: Float
)
