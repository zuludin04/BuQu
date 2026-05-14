package com.app.zuludin.buqu.ui.board.editor

sealed interface BoardEditorEvent {
    object SuccessSaveBoard: BoardEditorEvent
}