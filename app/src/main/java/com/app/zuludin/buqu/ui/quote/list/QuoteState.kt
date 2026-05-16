package com.app.zuludin.buqu.ui.quote.list

import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.Quote

data class QuoteState(
    val quotes: List<Quote> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val showCategoryFilter: Boolean = false,
    val selectedCategory: Category? = null,
    val searchQuery: String = "",
)