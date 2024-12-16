package com.app.zuludin.buqu.data.datasources.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class QuoteAndCategoryEntity(
    @Embedded
    val quote: QuoteEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity
)
