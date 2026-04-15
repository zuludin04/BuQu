package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity

@Dao
interface NoteCardDao {
    @Upsert
    suspend fun upsert(notes: List<NoteCardEntity>)

    @Query("DELETE FROM note_card WHERE noteId = :noteId")
    suspend fun deleteById(noteId: String)

    @Delete
    suspend fun deleteSelectedNotes(notes: List<NoteCardEntity>)

    @Query("SELECT * FROM note_card WHERE boardId = :boardId")
    suspend fun getNotesByBoard(boardId: String): List<NoteCardEntity>
}