package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.zuludin.buqu.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class BookDaoTest {
    private lateinit var database: BuQuDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BuQuDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertBook_success() = runTest {
        val book = DataDummy.generateBookDummy()[0]
        database.bookDao().upsert(book)

        val actual = database.bookDao().getById(book.bookId)
        assertNotNull(actual)
        assertEquals(book.bookId, actual?.bookId)
        assertEquals(book.title, actual?.title)
    }

    @Test
    fun deleteBook_success() = runTest {
        val book = DataDummy.generateBookDummy()[0]
        database.bookDao().upsert(book)

        database.bookDao().deleteById(book.bookId)

        val actual = database.bookDao().observeAllBooks().first()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun deleteBooks_success() = runTest {
        val books = DataDummy.generateBookDummy()
        books.forEach { database.bookDao().upsert(it) }

        database.bookDao().deleteBooks()

        val actual = database.bookDao().observeAllBooks().first()
        assertTrue(actual.isEmpty())
    }
}
