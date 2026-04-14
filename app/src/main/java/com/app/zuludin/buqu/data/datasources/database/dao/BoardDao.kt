package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM board")
    fun observeAllBoards(): Flow<List<BoardEntity>>

    @Query("SELECT * FROM board WHERE boardId = :boardId")
    suspend fun getById(boardId: String): BoardEntity?

    @Upsert
    suspend fun upsert(board: BoardEntity)

    @Query("DELETE FROM board WHERE boardId = :boardId")
    suspend fun deleteById(boardId: String)
}