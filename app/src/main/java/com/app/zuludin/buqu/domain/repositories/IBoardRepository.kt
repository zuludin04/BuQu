package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Board
import kotlinx.coroutines.flow.Flow

interface IBoardRepository {
    fun getBoards(): Flow<List<Board>>

    suspend fun getBoardById(boardId: String): Board?

    suspend fun upsertBoard(boardId: String?, name: String, color: String): String

    suspend fun deleteBoard(boardId: String)
}