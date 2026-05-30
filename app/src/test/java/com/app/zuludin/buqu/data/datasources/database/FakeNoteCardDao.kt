package com.app.zuludin.buqu.data.datasources.database

import com.app.zuludin.buqu.data.datasources.database.dao.NoteCardDao
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedNoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity

class FakeNoteCardDao(initialNotes: List<NoteCardEntity>? = emptyList()) : NoteCardDao {
    private var _notes: MutableMap<String, NoteCardEntity>? = null

    var notes: List<NoteCardEntity>?
        get() = _notes?.values?.toList()
        set(value) {
            _notes = value?.associateBy { it.noteId }?.toMutableMap()
        }

    init {
        notes = initialNotes
    }

    override suspend fun upsert(notes: List<NoteCardEntity>) {
        notes.forEach { _notes?.put(it.noteId, it) }
    }

    override suspend fun deleteById(noteId: String) {
        _notes?.remove(noteId)
    }

    override suspend fun deleteSelectedNotes(notes: List<NoteCardEntity>) {
        notes.forEach { _notes?.remove(it.noteId) }
    }

    override suspend fun getNotesByBoard(boardId: String): List<ConnectedNoteCardEntity> {
        return notes?.filter { it.boardId == boardId }?.map { it.toConnected() } ?: emptyList()
    }

    override suspend fun deleteNotesInBoard(boardId: String) {
        _notes?.entries?.removeIf { it.value.boardId == boardId }
    }
}

fun NoteCardEntity.toConnected(): ConnectedNoteCardEntity {
    return ConnectedNoteCardEntity(
        noteId = this.noteId,
        title = this.title,
        posX = this.posX,
        posY = this.posY,
        color = this.color,
        width = this.width,
        height = this.height,
        boardId = this.boardId,
        image = this.image,
        isConnected = 0
    )
}
