package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedRopeEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity

@Dao
interface RopeDao {
    @Upsert
    suspend fun upsert(ropes: List<RopeEntity>)

    @Query("DELETE FROM rope WHERE ropeId = :ropeId")
    suspend fun deleteById(ropeId: String)

    @Query("SELECT rope.*, source_note.posX AS sourceX, source_note.posY AS sourceY, target_note.posX AS targetX, target_note.posY AS targetY FROM rope INNER JOIN note_card AS source_note ON rope.sourceNoteId = source_note.noteId INNER JOIN note_card AS target_note ON rope.targetNoteId = target_note.noteId WHERE rope.boardId = :boardId")
    suspend fun getConnectedRopes(boardId: String): List<ConnectedRopeEntity>
}