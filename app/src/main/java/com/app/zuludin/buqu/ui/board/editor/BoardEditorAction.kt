package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.NoteCard

sealed interface BoardEditorAction {
    object OnOpenQuoteDialog : BoardEditorAction
    object OnOpenBookDialog : BoardEditorAction
    object DismissDialog : BoardEditorAction
    object OnImportBooks : BoardEditorAction
    object OnImportQuotes : BoardEditorAction
    object OnOpenNewBoardDialog : BoardEditorAction
    data class OnSaveBoard(val title: String, val color: String) : BoardEditorAction
    data class OnInputNoteDialog(val noteId: String?, val content: String = "") : BoardEditorAction
    data class OnGetBoardSize(val size: IntSize) : BoardEditorAction
    data class OnTransformChange(val zoom: Float, val offset: Offset) : BoardEditorAction
    data class OnChangeCameraZoom(val isZoomIn: Boolean) : BoardEditorAction
    object OnResetCamera : BoardEditorAction
    object OnDeleteSelectedNotes : BoardEditorAction
    object OnTidyUpNotes : BoardEditorAction
    object OnToggleGrid : BoardEditorAction
    data class OnDragNote(val note: NoteCard, val offset: Offset) : BoardEditorAction
    data class OnSelectNote(val noteId: String) : BoardEditorAction
    data class OnGetNoteSize(val size: IntSize, val index: Int) : BoardEditorAction
    data class OnConfirmInputNote(
        val title: String,
        val image: String,
        val color: String,
        val posX: Float? = null,
        val posY: Float? = null
    ) : BoardEditorAction

    data class OnUpdateNote(
        val noteId: String,
        val text: String,
        val image: String,
        val color: String
    ) : BoardEditorAction

    object OnDragEnd : BoardEditorAction
    object OnResetSelectedNotes : BoardEditorAction
    object DeleteBoard : BoardEditorAction
}