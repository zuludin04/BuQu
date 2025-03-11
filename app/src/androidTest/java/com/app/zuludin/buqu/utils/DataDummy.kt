package com.app.zuludin.buqu.utils

import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity

object DataDummy {
    fun generateQuoteDummy(): List<QuoteEntity> {
        val data = ArrayList<QuoteEntity>()
        for (i in 1..10) {
            val quote = QuoteEntity(
                quoteId = "Quote#$i",
                quote = "Hallo",
                author = "Asa",
                book = "Qoar",
                page = 10,
                categoryId = "Category$i"
            )
            data.add(quote)
        }
        return data
    }

    fun generateCategoryDummy(): List<CategoryEntity> {
        val data = ArrayList<CategoryEntity>()
        for (i in 1..10) {
            val category = CategoryEntity(
                categoryId = "Category$i",
                color = "000FFF",
                type = "Quote",
                name = "Motivation"
            )
            data.add(category)
        }
        return data
    }
}