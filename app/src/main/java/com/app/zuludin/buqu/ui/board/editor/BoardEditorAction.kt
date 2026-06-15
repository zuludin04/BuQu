package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote

sealed interface BoardEditorAction {
    data class OnOpenDialog(val state: BoardDialogState) : BoardEditorAction
    data class ConfirmInputNote(
        val noteId: String?,
        val content: String,
        val image: String,
        val color: String
    ) : BoardEditorAction

    data class TransformCamera(val offset: Offset, val zoom: Float) : BoardEditorAction
    data class DragNote(val noteCard: NoteCard, val offset: Offset) : BoardEditorAction
    data class OnSelectNote(val noteId: String) : BoardEditorAction
    object OnResetSelectedNotes : BoardEditorAction
    object DeleteBoard : BoardEditorAction
    object OnDeleteSelectedNotes : BoardEditorAction
    object OnTidyUpNotes : BoardEditorAction
    object OnToggleGrid : BoardEditorAction
    object OnCheckBoard : BoardEditorAction
    data class ConfirmUpsertBoard(val name: String, val color: String) : BoardEditorAction
    data class OnGetBoardSize(val size: IntSize) : BoardEditorAction
    data class OnGetNoteSize(val size: IntSize, val index: Int) : BoardEditorAction
    data class ConfirmImportBooks(val books: List<Book>) : BoardEditorAction
    data class ConfirmImportQuotes(val quotes: List<Quote>) : BoardEditorAction
    object OnDragEnd : BoardEditorAction
    data class OnConfirmConnectNote(val source: NoteCard, val target: NoteCard) : BoardEditorAction
    data class OnCanvasTap(val offset: Offset) : BoardEditorAction
    data class OnDeleteRope(val ropeId: String) : BoardEditorAction
    data class OnUpdateRope(val ropeId: String, val text: String, val color: String) :
        BoardEditorAction

    data class OnCreatePreviewRope(val handler: DragHandler, val dragAmount: Offset) :
        BoardEditorAction

    object OnDragArrowEnd : BoardEditorAction
}