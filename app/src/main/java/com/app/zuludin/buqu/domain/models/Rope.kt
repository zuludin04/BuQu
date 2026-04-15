package com.app.zuludin.buqu.domain.models

data class Rope(
    val ropeId: String,
    val sourceNoteId: String,
    val targetNoteId: String,
    val boardId: String,
    val sourceX: Float,
    val sourceY: Float,
    val targetX: Float,
    val targetY: Float
)
