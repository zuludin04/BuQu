package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.Rope

interface IRopeRepository {
    suspend fun upsertRopes(ropes: List<Rope>)

    suspend fun deleteRope(ropeId: String)

    suspend fun getConnectedRopes(boardId: String): List<Rope>
}