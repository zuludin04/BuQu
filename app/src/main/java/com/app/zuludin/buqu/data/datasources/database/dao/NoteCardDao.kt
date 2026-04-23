package com.app.zuludin.buqu.data.datasources.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedNoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity

@Dao
interface NoteCardDao {
    @Upsert
    suspend fun upsert(notes: List<NoteCardEntity>)

    @Query("DELETE FROM note_card WHERE noteId = :noteId")
    suspend fun deleteById(noteId: String)

    @Delete
    suspend fun deleteSelectedNotes(notes: List<NoteCardEntity>)

    @Query("SELECT note_card.*, CASE WHEN rope.ropeId IS NOT NULL THEN 1 ELSE 0 END AS isConnected FROM note_card LEFT JOIN rope ON note_card.noteId = rope.sourceNoteId OR note_card.noteId = rope.targetNoteId WHERE note_card.boardId = :boardId")
    suspend fun getNotesByBoard(boardId: String): List<ConnectedNoteCardEntity>
}