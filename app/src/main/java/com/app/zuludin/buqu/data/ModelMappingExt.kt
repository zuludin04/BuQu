package com.app.zuludin.buqu.data

import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteAndCategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote

fun Quote.toLocal() =
    QuoteEntity(
        quoteId = quoteId,
        quote = quote,
        author = author,
        book = book,
        page = page,
        categoryId = categoryId
    )

fun QuoteAndCategoryEntity.toExternal() =
    Quote(
        quote.quoteId,
        quote.quote,
        quote.author,
        quote.book,
        quote.page,
        category.categoryId,
        category.color,
        category.name
    )

@JvmName("localToExternalQuote")
fun List<QuoteAndCategoryEntity>.toExternal() = map(QuoteAndCategoryEntity::toExternal)

fun Category.toLocal() = CategoryEntity(categoryId, name, color, type)

fun CategoryEntity.toExternal() = Category(categoryId, name, color, type)

@JvmName("localToExternalCategory")
fun List<CategoryEntity>.toExternal() = map(CategoryEntity::toExternal)