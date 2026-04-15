package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.data.repositories.BoardRepository
import com.app.zuludin.buqu.data.repositories.NoteCardRepository
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class BoardEditorUiState(
    val notes: List<NoteCard> = emptyList(),
    val board: Board? = null
)

@HiltViewModel
class BoardEditorViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val noteRepository: NoteCardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val boardId: String? = savedStateHandle[BuquDestinationArgs.BOARD_ID_ARG]

    private val _uiState = MutableStateFlow(BoardEditorUiState())
    val uiState: StateFlow<BoardEditorUiState> = _uiState

    private lateinit var currentBoardId: String

    init {
        if (boardId != null) {
            loadData(boardId)
        } else {
            currentBoardId = UUID.randomUUID().toString()
        }
    }

    fun loadData(boardId: String) {
        loadBoard(boardId)
        loadNotes(boardId)
    }

    private fun loadBoard(boardId: String) {
        viewModelScope.launch {
            boardRepository.getBoardById(boardId).let { board ->
                if (board != null) {
                    _uiState.update {
                        it.copy(board = board)
                    }
                }
            }
        }
    }

    private fun loadNotes(boardId: String) {
        viewModelScope.launch {
            noteRepository.getNotesByBoard(boardId).let { notes ->
                _uiState.update {
                    it.copy(notes = notes)
                }
            }
        }
    }

    fun addNote(title: String) {
        val note = NoteCard(
            noteId = UUID.randomUUID().toString(),
            boardId = boardId ?: currentBoardId,
            title = title,
            posX = 100f,
            posY = 100f,
        )
        val notes = _uiState.value.notes.toMutableList()
        notes.add(note)
        _uiState.update {
            it.copy(notes = notes)
        }
    }

    fun dragNoteCard(noteId: String, x: Float, y: Float) {
        val note = _uiState.value.notes.first { it.noteId == noteId }
        val newNote = note.copy(posX = x, posY = y)
        val notes = _uiState.value.notes.toMutableList()
        val index = notes.indexOf(note)
        notes[index] = newNote
        _uiState.update { it.copy(notes = notes) }
    }

    fun getCardSize(size: IntSize, index: Int) {
        val note = _uiState.value.notes[index]
        val newNote = note.copy(size = size)
        val notes = _uiState.value.notes.toMutableList()
        notes[index] = newNote
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveBoarAndCards(name: String) {
        viewModelScope.launch {
            boardRepository.upsertBoard(boardId ?: currentBoardId, name, "000000")

            val notes = _uiState.value.notes
            noteRepository.upsertNotes(notes)
        }
    }
}