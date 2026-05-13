package com.app.zuludin.buqu.ui.book.edit

sealed interface BookEditEvent {
    object GoHome : BookEditEvent
    data class ShowSnackbar(val message: String) : BookEditEvent
}