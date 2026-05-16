package com.app.zuludin.buqu.ui.book.list

sealed interface BookAction {
    data class ChangeScope(val scope: BookSearchScope) : BookAction
    data class SearchBooks(val query: String) : BookAction
    object ClearQuery : BookAction
    data class BookSearchCta(val query: String) : BookAction
}