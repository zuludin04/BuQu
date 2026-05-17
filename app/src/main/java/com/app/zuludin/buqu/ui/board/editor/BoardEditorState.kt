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
    val noteHighlightId: String? = null,
    val previewRope: Rope? = null,
    val selectedNoteIds: List<String> = emptyList(),
    val showGrid: Boolean = true,
    val dialogState: BoardDialogState = BoardDialogState.None,
    val boardSize: IntSize = IntSize.Zero,
    val camera: Camera = Camera(),
    val showDelete: Boolean = false
)

sealed interface BoardDialogState {
    object None : BoardDialogState
    object ImportQuotes : BoardDialogState
    object ImportBooks : BoardDialogState
    object NewBoard : BoardDialogState
    data class AddNote(val note: NoteCard?, val isUpdate: Boolean) : BoardDialogState
}