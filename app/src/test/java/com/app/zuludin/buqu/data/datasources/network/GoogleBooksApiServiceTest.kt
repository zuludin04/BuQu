package com.app.zuludin.buqu.data.datasources.network

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleBooksApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: GoogleBooksApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun searchBooks_shouldReturnResponse() = runTest {
        val jsonResponse = """
            {
              "items": [
                {
                  "id": "1",
                  "volumeInfo": {
                    "title": "Android for Beginners",
                    "authors": ["John Doe"],
                    "description": "Learning Android"
                  }
                }
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.searchBooks("android", "fake_key")

        assertNotNull(result)
        assertEquals(1, result.items?.size)
        assertEquals("Android for Beginners", result.items?.get(0)?.volumeInfo?.title)
    }

    @Test
    fun getBookDetail_shouldReturnDetail() = runTest {
        val jsonResponse = """
            {
              "id": "123",
              "volumeInfo": {
                "title": "Clean Architecture",
                "authors": ["Uncle Bob"]
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.getBookDetail("123", "fake_key")

        assertNotNull(result)
        assertEquals("123", result.id)
        assertEquals("Clean Architecture", result.volumeInfo?.title)
    }
}
