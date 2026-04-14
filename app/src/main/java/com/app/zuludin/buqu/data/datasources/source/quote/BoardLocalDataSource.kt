package com.app.zuludin.buqu.data.datasources.source.quote

import com.app.zuludin.buqu.data.datasources.database.dao.BoardDao
import com.app.zuludin.buqu.data.datasources.database.dao.NoteCardDao
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSource @Inject constructor(
    private val boardDao: BoardDao,
    private val noteCardDao: NoteCardDao
) : IBoardLocalDataSource {
    override fun getBoards(): Flow<List<BoardEntity>> = boardDao.observeAllBoards()

    override suspend fun getBoardById(boardId: String): BoardEntity? = boardDao.getById(boardId)

    override suspend fun upsertBoard(board: BoardEntity) = boardDao.upsert(board)

    override suspend fun deleteBoard(boardId: String) = boardDao.deleteById(boardId)

    override suspend fun upsertNotes(cards: List<NoteCardEntity>) = noteCardDao.upsert(cards)

    override suspend fun deleteNote(noteId: String) = noteCardDao.deleteById(noteId)

    override suspend fun getNotesByBoard(boardId: String): List<NoteCardEntity> =
        noteCardDao.getNotesByBoard(boardId)
}