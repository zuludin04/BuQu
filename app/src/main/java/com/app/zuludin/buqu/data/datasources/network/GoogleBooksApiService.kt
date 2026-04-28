package com.app.zuludin.buqu.data.datasources.network

import com.app.zuludin.buqu.data.datasources.network.response.BookDetailResponse
import com.app.zuludin.buqu.data.datasources.network.response.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int = 10,
        @Query("printType") printType: String = "books"
    ): GoogleBooksResponse

    @GET("volumes/{id}")
    suspend fun getBookDetail(
        @Path("id") id: String,
        @Query("key") key: String
    ): BookDetailResponse
}
