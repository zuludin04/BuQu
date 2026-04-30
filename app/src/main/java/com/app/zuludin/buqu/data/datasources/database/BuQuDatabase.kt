package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.zuludin.buqu.data.datasources.database.dao.BoardDao
import com.app.zuludin.buqu.data.datasources.database.dao.BookDao
import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.NoteCardDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.dao.RopeDao
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity

@Database(
    entities = [QuoteEntity::class, CategoryEntity::class, BoardEntity::class, NoteCardEntity::class, RopeEntity::class, BookEntity::class],
    version = 2,
    exportSchema = true
)
abstract class BuQuDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    abstract fun categoryDao(): CategoryDao

    abstract fun boardDao(): BoardDao

    abstract fun noteCardDao(): NoteCardDao

    abstract fun ropeDao(): RopeDao

    abstract fun bookDao(): BookDao
}
