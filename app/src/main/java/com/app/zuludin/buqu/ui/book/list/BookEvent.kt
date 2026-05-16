package com.app.zuludin.buqu.ui.book.list

sealed interface BookEvent {
    data class ErrorOnline(val message: String): BookEvent
}