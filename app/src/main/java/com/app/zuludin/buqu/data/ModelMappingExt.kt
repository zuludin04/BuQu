package com.app.zuludin.buqu.data

import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.domain.models.Quote

fun Quote.toLocal() =
    QuoteEntity(quoteId = quoteId, quote = quote, author = author, book = "", page = 1)

fun QuoteEntity.toExternal() = Quote(quoteId, quote, author)

@JvmName("localToExternal")
fun List<QuoteEntity>.toExternal() = map(QuoteEntity::toExternal)