package com.app.zuludin.buqu.ui.board.editor

import com.app.zuludin.buqu.domain.models.Rope

sealed interface BoardEditorEvent {
    object SuccessSaveBoard: BoardEditorEvent
    object GoHome: BoardEditorEvent
    data class CreateConnectedRope(val rope: Rope): BoardEditorEvent
}