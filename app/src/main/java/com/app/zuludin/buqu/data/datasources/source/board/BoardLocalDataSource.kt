package com.app.zuludin.buqu.data.datasources.source.board

import com.app.zuludin.buqu.data.datasources.database.dao.BoardDao
import com.app.zuludin.buqu.data.datasources.database.dao.NoteCardDao
import com.app.zuludin.buqu.data.datasources.database.dao.RopeDao
import com.app.zuludin.buqu.data.datasources.database.entities.BoardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.BoardTotalNoteEntity
import com.app.zuludin.buqu.data.datasources.database.entities.ConnectedRopeEntity
import com.app.zuludin.buqu.data.datasources.database.entities.NoteCardEntity
import com.app.zuludin.buqu.data.datasources.database.entities.RopeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSource @Inject constructor(
    private val boardDao: BoardDao,
    private val noteCardDao: NoteCardDao,
    private val ropeDao: RopeDao
) : IBoardLocalDataSource {
    override fun getBoards(): Flow<List<BoardEntity>> = boardDao.observeAllBoards()

    override fun getBoardTotalNote(): Flow<List<BoardTotalNoteEntity>> =
        boardDao.observeBoardTotalNote()

    override suspend fun getBoardById(boardId: String): BoardEntity? = boardDao.getById(boardId)

    override suspend fun upsertBoard(board: BoardEntity) = boardDao.upsert(board)

    override suspend fun deleteBoard(boardId: String) = boardDao.deleteById(boardId)

    override suspend fun upsertNotes(cards: List<NoteCardEntity>) = noteCardDao.upsert(cards)

    override suspend fun deleteNote(noteId: String) = noteCardDao.deleteById(noteId)

    override suspend fun getNotesByBoard(boardId: String): List<NoteCardEntity> =
        noteCardDao.getNotesByBoard(boardId)

    override suspend fun upsertRopes(ropes: List<RopeEntity>) = ropeDao.upsert(ropes)

    override suspend fun deleteRope(ropeId: String) = ropeDao.deleteById(ropeId)

    override suspend fun getConnectedRopes(boardId: String): List<ConnectedRopeEntity> =
        ropeDao.getConnectedRopes(boardId)

    override suspend fun deleteSelectedNotes(notes: List<NoteCardEntity>) =
        noteCardDao.deleteSelectedNotes(notes)

    override suspend fun deleteSelectedRopes(ropes: List<RopeEntity>) =
        ropeDao.deleteSelectedRopes(ropes)
}