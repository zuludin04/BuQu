package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.data.datasources.source.board.BoardLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RopeRepository @Inject constructor(
    private val localSource: BoardLocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : IRopeRepository {
    override suspend fun upsertRopes(ropes: List<Rope>) {
        localSource.upsertRopes(ropes.toLocal())
    }

    override suspend fun deleteRope(ropeId: String) {
        localSource.deleteRope(ropeId)
    }

    override suspend fun getConnectedRopes(boardId: String): List<Rope> {
        return localSource.getConnectedRopes(boardId).map { ropes ->
            withContext(dispatcher) {
                ropes.toExternal()
            }
        }
    }

    override suspend fun deleteSelectedRopes(ropes: List<Rope>) {
        localSource.deleteSelectedRopes(ropes.toLocal())
    }
}