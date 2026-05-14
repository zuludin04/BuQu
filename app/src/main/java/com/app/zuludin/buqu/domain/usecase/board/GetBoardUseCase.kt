package com.app.zuludin.buqu.domain.usecase.board

import com.app.zuludin.buqu.domain.models.BoardEditorData
import com.app.zuludin.buqu.domain.repositories.IBoardRepository
import com.app.zuludin.buqu.domain.repositories.IBookRepository
import com.app.zuludin.buqu.domain.repositories.INoteCardRepository
import com.app.zuludin.buqu.domain.repositories.IQuoteRepository
import com.app.zuludin.buqu.domain.repositories.IRopeRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetBoardUseCase @Inject constructor(
    private val boardRepository: IBoardRepository,
    private val noteRepository: INoteCardRepository,
    private val ropeRepository: IRopeRepository,
    private val quoteRepository: IQuoteRepository,
    private val bookRepository: IBookRepository,
) {
    suspend operator fun invoke(boardId: String?): BoardEditorData {
        val quotes = quoteRepository.loadQuotes()
        val books = bookRepository.observeBooks().first()

        if (boardId != null) {
            val board = boardRepository.getBoardById(boardId)
            val notes = noteRepository.getNotesByBoard(boardId)
            val ropes = ropeRepository.getConnectedRopes(boardId)

            return BoardEditorData(
                board = board,
                notes = notes,
                ropes = ropes,
                quotes = quotes,
                books = books
            )
        } else {
            return BoardEditorData(quotes = quotes, books = books)
        }
    }
}