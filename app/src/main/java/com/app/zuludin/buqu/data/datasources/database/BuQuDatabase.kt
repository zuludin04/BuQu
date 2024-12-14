package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.zuludin.buqu.data.datasources.database.dao.CategoryDao
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity

@Database(entities = [QuoteEntity::class, CategoryEntity::class], version = 1, exportSchema = true)
abstract class BuQuDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    abstract fun categoryDao(): CategoryDao
}