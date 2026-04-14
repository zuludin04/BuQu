package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.data.datasources.source.quote.BoardLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardRepository @Inject constructor(
    private val localSource: BoardLocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : IBoardRepository {
    override fun getBoards(): Flow<List<Board>> {
        return localSource.getBoards().map { boards ->
            withContext(dispatcher) {
                boards.toExternal()
            }
        }
    }

    override suspend fun getBoardById(boardId: String): Board? {
        return localSource.getBoardById(boardId)?.toExternal()
    }

    override suspend fun upsertBoard(
        boardId: String?,
        name: String,
        color: String
    ): String {
        val id = boardId ?: withContext(dispatcher) {
            UUID.randomUUID().toString()
        }

        val board = Board(
            boardId = id,
            name = name,
            color = color
        )
        localSource.upsertBoard(board.toLocal())

        return id
    }

    override suspend fun deleteBoard(boardId: String) {
        localSource.deleteBoard(boardId)
    }
}