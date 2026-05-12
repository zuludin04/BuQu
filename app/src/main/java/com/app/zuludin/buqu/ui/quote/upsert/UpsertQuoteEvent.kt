package com.app.zuludin.buqu.ui.quote.upsert

sealed interface UpsertQuoteEvent {
    data class ShowSnackbar(val message: String) : UpsertQuoteEvent
    object GoHome : UpsertQuoteEvent
}