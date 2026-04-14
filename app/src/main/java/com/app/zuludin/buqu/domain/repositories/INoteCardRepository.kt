package com.app.zuludin.buqu.domain.repositories

import com.app.zuludin.buqu.domain.models.NoteCard

interface INoteCardRepository {
    suspend fun getNotesByBoard(boardId: String): List<NoteCard>

    suspend fun upsertNotes(notes: List<NoteCard>)

    suspend fun deleteNote(noteId: String)
}