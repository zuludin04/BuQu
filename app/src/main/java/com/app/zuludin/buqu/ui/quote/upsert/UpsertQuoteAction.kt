package com.app.zuludin.buqu.ui.quote.upsert

import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Category

sealed interface UpsertQuoteAction {
    data class PickImage(val path: String) : UpsertQuoteAction
    data class UpdateQuote(val content: String) : UpsertQuoteAction
    object DeleteQuote : UpsertQuoteAction
    data class ToggleSavingMode(val isImage: Boolean) : UpsertQuoteAction
    object RemoveImage : UpsertQuoteAction
    data class SelectBook(val book: Book?) : UpsertQuoteAction
    data class SelectCategory(val category: Category) : UpsertQuoteAction
    object SaveQuote : UpsertQuoteAction
    data class ScanTextFromImage(val text: String) : UpsertQuoteAction
}