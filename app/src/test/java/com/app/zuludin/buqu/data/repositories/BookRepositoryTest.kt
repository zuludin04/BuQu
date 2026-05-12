package com.app.zuludin.buqu.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.data.datasources.network.GoogleBooksApiService
import com.app.zuludin.buqu.data.datasources.network.response.BookItem
import com.app.zuludin.buqu.data.datasources.network.response.GoogleBooksResponse
import com.app.zuludin.buqu.data.datasources.network.response.VolumeInfo
import com.app.zuludin.buqu.data.datasources.source.book.BookLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BookRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var localSource: BookLocalDataSource

    @Mock
    private lateinit var apiService: GoogleBooksApiService

    private var testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: BookRepository

    @Before
    fun setUp() {
        repository = BookRepository(localSource, apiService, testDispatcher)
    }

    @Test
    fun searchBooks_success() = runTest {
        val query = "Android"
        val mockResponse = GoogleBooksResponse(
            items = listOf(
                BookItem(
                    id = "1",
                    volumeInfo = VolumeInfo(
                        title = "Android Programming",
                        authors = listOf("Author 1"),
                        description = "Description",
                        imageLinks = null,
                        pageCount = 100,
                        publisher = "Publisher",
                        publishedDate = "2023-01-01",
                    )
                )
            )
        )

        `when`(apiService.searchBooks(query, BuildConfig.GOOGLE_BOOKS_API_KEY)).thenReturn(mockResponse)

        val actual = repository.searchBooks(query)

        assertNotNull(actual)
        assertEquals(1, actual.size)
        assertEquals("Android Programming", actual[0].title)
        assertEquals("Author 1", actual[0].author)
    }

    @Test
    fun searchBooks_emptyResponse() = runTest {
        val query = "UnknownBook"
        val mockResponse = GoogleBooksResponse(items = null)

        `when`(apiService.searchBooks(query, BuildConfig.GOOGLE_BOOKS_API_KEY)).thenReturn(mockResponse)

        val actual = repository.searchBooks(query)

        assertNotNull(actual)
        assertTrue(actual.isEmpty())
    }

    @Test
    fun searchBooks_exception() = runTest {
        val query = "Error"
        `when`(apiService.searchBooks(query, BuildConfig.GOOGLE_BOOKS_API_KEY)).thenThrow(RuntimeException())

        val actual = repository.searchBooks(query)

        assertNotNull(actual)
        assertTrue(actual.isEmpty())
    }
}
