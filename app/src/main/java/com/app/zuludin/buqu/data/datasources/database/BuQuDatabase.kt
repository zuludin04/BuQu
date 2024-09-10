package com.app.zuludin.buqu.data.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.data.datasources.database.dao.QuoteDao

@Database(entities = [QuoteEntity::class], version = 1, exportSchema = false)
abstract class BuQuDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}