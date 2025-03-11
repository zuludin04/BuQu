package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class QuoteDaoTest {
    private lateinit var database: BuQuDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BuQuDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertQuote_success() = runTest {
        // need to add category because quote entity have connection with category entity
        val cat = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(cat)

        val quote = QuoteEntity("quote1", "Hallo", "Asa", "Es", 12, cat.categoryId)
        database.quoteDao().upsert(quote)

        val actual = database.quoteDao().getQuoteDetail(quote.quoteId)
        assertNotNull(actual)
        assertEquals(quote.quote, actual?.quote)
        assertEquals(quote.author, actual?.author)
    }

    @Test
    fun deleteQuote_success() = runTest {
        // need to add category because quote entity have connection with category entity
        val cat = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(cat)

        val quote = QuoteEntity("quote1", "Hallo", "Asa", "Es", 12, cat.categoryId)
        database.quoteDao().upsert(quote)

        database.quoteDao().deleteById(quote.quoteId)

        val actual = database.quoteDao().observeAllQuotes().first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun updateQuote_success() = runTest {
        // need to add category because quote entity have connection with category entity
        val cat = DataDummy.generateCategoryDummy()[0]
        database.categoryDao().upsert(cat)

        val oldQuote = QuoteEntity("quote1", "Hallo", "Asa", "Es", 12, cat.categoryId)
        database.quoteDao().upsert(oldQuote)

        val newQuote = QuoteEntity("quote1", "Hallo 12", "Asa", "Es", 15, cat.categoryId)

        database.quoteDao().upsert(newQuote)

        val actual = database.quoteDao().getQuoteDetail(newQuote.quoteId)
        assertNotNull(actual)
        assertEquals(oldQuote.quoteId, actual?.quoteId)
        assertEquals(newQuote.quote, actual?.quote)
        assertEquals(newQuote.page, actual?.page)
    }
}