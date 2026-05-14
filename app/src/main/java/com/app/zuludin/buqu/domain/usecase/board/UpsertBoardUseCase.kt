package com.app.zuludin.buqu.domain.usecase.board

import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import javax.inject.Inject

class UpsertBoardUseCase @Inject constructor(
    private val boardRepository: IBoardRepository,
    private val noteRepository: INoteCardRepository,
    private val ropeRepository: IRopeRepository,
) {
    suspend operator fun invoke(board: Board, notes: List<NoteCard>, ropes: List<Rope>) {
        boardRepository.upsertBoard(board.boardId, board.name, board.color)

        val activeNotes = notes.filter { it.status == "active" }
        val deletedNotes = notes.filter { it.status == "deleted" }

        noteRepository.upsertNotes(activeNotes)
        if (deletedNotes.isNotEmpty()) noteRepository.deleteSelectedNotes(deletedNotes)

        val activeRopes = ropes.filter { it.status == "active" }
        val deletedRopes = ropes.filter { it.status == "deleted" }

        ropeRepository.upsertRopes(activeRopes)
        if (deletedRopes.isNotEmpty()) ropeRepository.deleteSelectedRopes(deletedRopes)
    }
}