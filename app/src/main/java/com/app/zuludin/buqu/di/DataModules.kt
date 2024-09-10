package com.app.zuludin.buqu.di

import android.content.Context
import androidx.room.Room
import com.app.zuludin.buqu.data.datasources.database.BuQuDatabase
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuoteRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindQuoteRepository(repository: QuoteRepository): IQuoteRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BuQuDatabase {
        return Room.databaseBuilder(context.applicationContext, BuQuDatabase::class.java, "Buqu.db")
            .build()
    }

    @Provides
    fun provideQuoteDao(database: BuQuDatabase) = database.quoteDao()
}