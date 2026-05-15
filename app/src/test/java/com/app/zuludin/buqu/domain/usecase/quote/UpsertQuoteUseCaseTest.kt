package com.app.zuludin.buqu.domain.usecase.quote

import com.app.zuludin.buqu.domain.models.InvalidQuoteException
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpsertQuoteUseCaseTest {

    @Mock
    private lateinit var quoteRepository: IQuoteRepository

    private lateinit var upsertQuoteUseCase: UpsertQuoteUseCase

    @Before
    fun setUp() {
        upsertQuoteUseCase = UpsertQuoteUseCase(quoteRepository)
    }

    @Test
    fun `invoke with valid quote should call repository upsert`() = runTest {
        val quote = Quote(
            quoteId = "1",
            quote = "Valid quote",
            author = "Author",
            book = "Book",
            page = 1,
            categoryId = "1"
        )

        upsertQuoteUseCase(quote.quoteId, quote)

        verify(quoteRepository).upsertQuote(
            quoteId = quote.quoteId,
            quote = quote.quote,
            author = quote.author,
            book = quote.book,
            page = quote.page,
            categoryId = quote.categoryId,
            bookId = quote.bookId,
            image = quote.image
        )
    }

    @Test(expected = InvalidQuoteException::class)
    fun `invoke with empty quote and image should throw InvalidQuoteException`() = runTest {
        val quote = Quote(
            quoteId = "1",
            quote = "",
            author = "Author",
            book = "Book",
            page = 1,
            categoryId = "1",
            image = ""
        )

        upsertQuoteUseCase(quote.quoteId, quote)
    }
}
