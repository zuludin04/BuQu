package com.app.zuludin.buqu.domain.models

data class BoardEditorData(
    val board: Board? = null,
    val notes: List<NoteCard> = emptyList(),
    val ropes: List<Rope> = emptyList(),
    val quotes: List<Quote> = emptyList(),
    val books: List<Book> = emptyList()
)