package com.app.zuludin.buqu.data.datasources.network.response

data class GoogleBooksResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val pageCount: Int?,
    val publisher: String? = null,
    val publishedDate: String? = null
)

data class ImageLinks(
    val thumbnail: String?
)