package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.RopeDao
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedRopeEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity

class FakeRopeDao(initialRopes: List<RopeEntity>? = emptyList()) : RopeDao {
    private var _ropes: MutableMap<String, RopeEntity>? = null

    var ropes: List<RopeEntity>?
        get() = _ropes?.values?.toList()
        set(value) {
            _ropes = value?.associateBy { it.ropeId }?.toMutableMap()
        }

    init {
        ropes = initialRopes
    }

    override suspend fun upsert(ropes: List<RopeEntity>) {
        ropes.forEach { _ropes?.put(it.ropeId, it) }
    }

    override suspend fun deleteById(ropeId: String) {
        _ropes?.remove(ropeId)
    }

    override suspend fun deleteSelectedRopes(ropes: List<RopeEntity>) {
        ropes.forEach { _ropes?.remove(it.ropeId) }
    }

    override suspend fun getConnectedRopes(boardId: String): List<ConnectedRopeEntity> {
        return ropes?.filter { it.boardId == boardId }?.map { it.toConnected() } ?: emptyList()
    }
}

fun RopeEntity.toConnected(): ConnectedRopeEntity {
    return ConnectedRopeEntity(
        ropeId = this.ropeId,
        sourceNoteId = this.sourceNoteId,
        targetNoteId = this.targetNoteId,
        boardId = this.boardId,
        sourceX = 0f,
        sourceY = 0f,
        targetX = 0f,
        targetY = 0f,
        sourceWidth = 0,
        sourceHeight = 0,
        targetWidth = 0,
        targetHeight = 0
    )
}
