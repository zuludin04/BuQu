package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.BoardDao
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.BoardTotalNoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBoardDao(initialBoards: List<BoardEntity>? = emptyList()) : BoardDao {
    private var _boards: MutableMap<String, BoardEntity>? = null

    var boards: List<BoardEntity>?
        get() = _boards?.values?.toList()
        set(value) {
            _boards = value?.associateBy { it.boardId }?.toMutableMap()
        }

    init {
        boards = initialBoards
    }

    override fun observeAllBoards(): Flow<List<BoardEntity>> {
        return flow { emit(boards ?: emptyList()) }
    }

    override fun observeBoardTotalNote(): Flow<List<BoardTotalNoteEntity>> {
        val data = boards?.map { 
            BoardTotalNoteEntity(it.boardId, it.name, it.color, 0)
        } ?: emptyList()
        return flow { emit(data) }
    }

    override suspend fun getById(boardId: String): BoardEntity? {
        return _boards?.get(boardId)
    }

    override suspend fun upsert(board: BoardEntity) {
        _boards?.put(board.boardId, board)
    }

    override suspend fun deleteById(boardId: String) {
        _boards?.remove(boardId)
    }
}
