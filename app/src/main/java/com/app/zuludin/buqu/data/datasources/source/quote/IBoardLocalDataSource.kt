package com.app.zuludin.buqu.data.datasources.source.quote

import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import kotlinx.coroutines.flow.Flow

interface IBoardLocalDataSource {
    fun getBoards(): Flow<List<BoardEntity>>

    suspend fun getBoardById(boardId: String): BoardEntity?

    suspend fun upsertBoard(board: BoardEntity)

    suspend fun deleteBoard(boardId: String)

    suspend fun upsertNotes(cards: List<NoteCardEntity>)

    suspend fun deleteNote(noteId: String)

    suspend fun getNotesByBoard(boardId: String): List<NoteCardEntity>
}