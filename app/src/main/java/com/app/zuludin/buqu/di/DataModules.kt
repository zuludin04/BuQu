package com.app.zuludin.buqu.di

import android.content.Context
import androidx.room.Room
import com.app.zuludin.buqu.core.utils.PrePopulateCategoryCallback
import com.app.zuludin.buqu.data.datasources.database.BuQuDatabase
import com.app.zuludin.buqu.data.repositories.BoardRepository
import com.app.zuludin.buqu.data.repositories.BookRepository
import com.app.zuludin.buqu.data.repositories.CategoryRepository
import com.app.zuludin.buqu.data.repositories.NoteCardRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.data.repositories.RopeRepository
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.ICategoryRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
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

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepository): ICategoryRepository

    @Singleton
    @Binds
    abstract fun bindBookRepository(repository: BookRepository): IBookRepository

    @Singleton
    @Binds
    abstract fun bindBoardRepository(repository: BoardRepository): IBoardRepository

    @Singleton
    @Binds
    abstract fun bindNoteRepository(repository: NoteCardRepository): INoteCardRepository

    @Singleton
    @Binds
    abstract fun bundRopeRepository(repository: RopeRepository): IRopeRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BuQuDatabase {
        return Room
            .databaseBuilder(context.applicationContext, BuQuDatabase::class.java, "Buqu.db")
            .addCallback(PrePopulateCategoryCallback())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideQuoteDao(database: BuQuDatabase) = database.quoteDao()

    @Provides
    fun provideCategoryDao(database: BuQuDatabase) = database.categoryDao()

    @Provides
    fun provideBoardDao(database: BuQuDatabase) = database.boardDao()

    @Provides
    fun provideNoteCardDao(database: BuQuDatabase) = database.noteCardDao()

    @Provides
    fun provideRopeDao(database: BuQuDatabase) = database.ropeDao()

    @Provides
    fun provideBookDao(database: BuQuDatabase) = database.bookDao()
}
