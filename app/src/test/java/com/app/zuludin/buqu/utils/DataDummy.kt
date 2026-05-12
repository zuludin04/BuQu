package com.app.zuludin.buqu.utils

import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.BookEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity

object DataDummy {
    fun generateQuoteDummy(): List<QuoteEntity> {
        val data = ArrayList<QuoteEntity>()
        for (i in 1..10) {
            val quote = QuoteEntity(
                quoteId = "Quote#$i",
                quote = "Hallo",
                author = "Asa",
                book = "Qoar",
                page = 10,
                categoryId = "Category$i",
                image = ""
            )
            data.add(quote)
        }
        return data
    }

    fun generateCategoryDummy(): List<CategoryEntity> {
        val data = ArrayList<CategoryEntity>()
        for (i in 1..10) {
            val category = CategoryEntity(
                categoryId = "Category$i",
                color = "000FFF",
                type = "Quote",
                name = "Motivation"
            )
            data.add(category)
        }
        return data
    }

    fun generateBoardDummy(): List<BoardEntity> {
        val data = ArrayList<BoardEntity>()
        for (i in 1..5) {
            val board = BoardEntity(
                boardId = "Board$i",
                name = "Board Name $i",
                color = "FFFFFF"
            )
            data.add(board)
        }
        return data
    }

    fun generateNoteCardDummy(boardId: String): List<NoteCardEntity> {
        val data = ArrayList<NoteCardEntity>()
        for (i in 1..5) {
            val note = NoteCardEntity(
                noteId = "Note$i",
                title = "Note Title $i",
                posX = 0f,
                posY = 0f,
                color = "FFFFFF",
                width = 100,
                height = 100,
                boardId = boardId,
                image = ""
            )
            data.add(note)
        }
        return data
    }

    fun generateBookDummy(): List<BookEntity> {
        val data = ArrayList<BookEntity>()
        for (i in 1..5) {
            val book = BookEntity(
                bookId = "Book$i",
                title = "Book Title $i",
                author = "Author $i",
                cover = "",
                description = "Description $i",
                totalPages = 100,
                publisher = "Publisher $i",
                year = 2023
            )
            data.add(book)
        }
        return data
    }

    fun generateRopeDummy(boardId: String, sourceId: String, targetId: String): RopeEntity {
        return RopeEntity(
            ropeId = "Rope1",
            sourceNoteId = sourceId,
            targetNoteId = targetId,
            boardId = boardId
        )
    }
}
