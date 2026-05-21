package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Camera
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope

data class BoardEditorState(
    val board: Board? = null,
    val notes: List<NoteCard> = emptyList(),
    val ropes: List<Rope> = emptyList(),
    val quotes: List<Quote> = emptyList(),
    val books: List<Book> = emptyList(),
    val selectedNoteIds: List<String> = emptyList(),
    val dialogState: BoardDialogState = BoardDialogState.None,
    val camera: Camera = Camera(),
    val showGrid: Boolean = true,
    val boardSize: IntSize = IntSize.Zero,
    val noteHighlightId: String? = null,
    val previewRope: Rope? = null,
)

sealed interface BoardDialogState {
    object None : BoardDialogState
    data class InputNoteDialog(
        val noteId: String?,
        val title: String,
        val color: String
    ) : BoardDialogState

    data class UpsertBoardDialog(val name: String? = null, val color: String? = null) :
        BoardDialogState

    object ImportQuotes : BoardDialogState
    object ImportBooks : BoardDialogState
}