package com.app.zuludin.buqu.ui.board.editor

import com.app.zuludin.buqu.domain.models.NoteCard

sealed interface BoardEditorAction {
    object OnOpenQuoteDialog : BoardEditorAction
    object OnOpenBookDialog : BoardEditorAction
    object DismissDialog : BoardEditorAction
    object OnImportBooks : BoardEditorAction
    object OnImportQuotes : BoardEditorAction
    object OnOpenNewBoardDialog : BoardEditorAction
    data class OnSaveBoard(val title: String, val color: String) : BoardEditorAction
    data class OnOpenAddNoteDialog(val note: NoteCard?, val isUpdate: Boolean) : BoardEditorAction
}