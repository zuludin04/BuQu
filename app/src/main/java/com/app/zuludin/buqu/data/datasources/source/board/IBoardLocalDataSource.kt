package com.app.zuludin.buqu.data.datasources.source.board

import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedRopeEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity
import kotlinx.coroutines.flow.Flow

interface IBoardLocalDataSource {
    fun getBoards(): Flow<List<BoardEntity>>

    suspend fun getBoardById(boardId: String): BoardEntity?

    suspend fun upsertBoard(board: BoardEntity)

    suspend fun deleteBoard(boardId: String)

    suspend fun upsertNotes(cards: List<NoteCardEntity>)

    suspend fun deleteNote(noteId: String)

    suspend fun getNotesByBoard(boardId: String): List<NoteCardEntity>

    suspend fun upsertRopes(ropes: List<RopeEntity>)

    suspend fun deleteRope(ropeId: String)

    suspend fun getConnectedRopes(boardId: String): List<ConnectedRopeEntity>
}