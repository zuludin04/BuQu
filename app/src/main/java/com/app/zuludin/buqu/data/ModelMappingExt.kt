package com.app.zuludin.buqu.data

import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryAndQuoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.CategoryEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.QuoteEntity
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.Category
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote

fun Quote.toLocal() =
    QuoteEntity(
        quoteId = quoteId,
        quote = quote,
        author = author,
        book = book,
        page = page,
        categoryId = categoryId
    )

fun CategoryAndQuoteEntity.toExternal() =
    Quote(
        quoteId,
        quote,
        author,
        book,
        page,
        categoryId,
        color,
        name
    )

@JvmName("localToExternalQuote")
fun List<CategoryAndQuoteEntity>.toExternal() = map(CategoryAndQuoteEntity::toExternal)

fun Category.toLocal() = CategoryEntity(categoryId, name, color, type)

fun CategoryEntity.toExternal() = Category(categoryId, name, color, type)

@JvmName("localToExternalCategory")
fun List<CategoryEntity>.toExternal() = map(CategoryEntity::toExternal)

fun Board.toLocal() = BoardEntity(
    boardId = boardId,
    name = name,
    color = color
)

fun BoardEntity.toExternal() = Board(
    boardId = boardId,
    name = name,
    color = color
)

@JvmName("localToExternalBoard")
fun List<BoardEntity>.toExternal() = map(BoardEntity::toExternal)

fun NoteCard.toLocal() = NoteCardEntity(
    noteId = noteId,
    title = title,
    posX = posX,
    posY = posY,
    boardId = boardId
)

fun NoteCardEntity.toExternal() = NoteCard(
    noteId = noteId,
    title = title,
    posX = posX,
    posY = posY,
    boardId = boardId
)

@JvmName("externalToLocalNoteCard")
fun List<NoteCard>.toLocal() = map(NoteCard::toLocal)