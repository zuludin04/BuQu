package com.app.zuludin.buqu.domain.usecase.board

import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import javax.inject.Inject

class DeleteBoardUseCase @Inject constructor(
    private val boardRepository: IBoardRepository,
    private val noteRepository: INoteCardRepository,
    private val ropeRepository: IRopeRepository,
) {
    suspend operator fun invoke(boardId: String) {
        boardRepository.deleteBoard(boardId)
        noteRepository.deleteNotesInBoard(boardId)
        ropeRepository.deleteRopesInBoard(boardId)
    }
}