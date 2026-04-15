package com.app.zuludin.buqu.data.repositories

import com.app.zuludin.buqu.data.datasources.source.board.BoardLocalDataSource
import com.app.zuludin.buqu.data.toExternal
import com.app.zuludin.buqu.data.toLocal
import com.app.zuludin.buqu.di.DefaultDispatcher
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCardRepository @Inject constructor(
    private val localSource: BoardLocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : INoteCardRepository {
    override suspend fun getNotesByBoard(boardId: String): List<NoteCard> {
        return localSource.getNotesByBoard(boardId).map { notes ->
            withContext(dispatcher) {
                notes.toExternal()
            }
        }
    }

    override suspend fun upsertNotes(notes: List<NoteCard>) {
        localSource.upsertNotes(notes.toLocal())
    }

    override suspend fun deleteNote(noteId: String) {
        localSource.deleteNote(noteId)
    }
}