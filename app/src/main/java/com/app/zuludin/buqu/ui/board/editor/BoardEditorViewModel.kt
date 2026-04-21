package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.data.repositories.BoardRepository
import com.app.zuludin.buqu.data.repositories.NoteCardRepository
import com.app.zuludin.buqu.data.repositories.QuoteRepository
import com.app.zuludin.buqu.data.repositories.RopeRepository
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope
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
    val board: Board? = null,
    val ropes: List<Rope> = emptyList(),
    val sourceNote: NoteCard? = null,
    val isSelectionMode: Boolean = false,
    val selectedNoteIds: List<String> = emptyList(),
    val deletedNotes: List<NoteCard> = emptyList(),
    val deletedRopes: List<Rope> = emptyList(),
    val isConnectionMode: Boolean = false,
    val errorConnectSameNote: Boolean = false,
    val successSaveBoard: Boolean = false,
    val quotes: List<Quote> = emptyList()
)

@HiltViewModel
class BoardEditorViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val noteRepository: NoteCardRepository,
    private val ropeRepository: RopeRepository,
    private val quoteRepository: QuoteRepository,
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
        loadQuotes()
    }

    fun loadData(boardId: String) {
        loadBoard(boardId)
        loadNotes(boardId)
        loadRopes(boardId)
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

    private fun loadRopes(boardId: String) {
        viewModelScope.launch {
            ropeRepository.getConnectedRopes(boardId).let { ropes ->
                _uiState.update {
                    it.copy(ropes = ropes)
                }
            }
        }
    }

    fun addNote(title: String, color: String, posX: Float = 100f, posY: Float = 100f) {
        val note = NoteCard(
            noteId = UUID.randomUUID().toString(),
            boardId = boardId ?: currentBoardId,
            title = title,
            posX = posX,
            posY = posY,
            color = color,
            size = IntSize.Zero
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
        updateRopePosition(note)
    }

    fun getCardSize(size: IntSize, index: Int) {
        val note = _uiState.value.notes[index]
        val newNote = note.copy(size = size)
        val notes = _uiState.value.notes.toMutableList()
        notes[index] = newNote
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveBoardAndCards(name: String, color: String = "000000") {
        viewModelScope.launch {
            boardRepository.upsertBoard(boardId ?: currentBoardId, name, color)
            _uiState.update {
                it.copy(
                    board = Board(boardId ?: currentBoardId, name, color),
                    successSaveBoard = true
                )
            }

            val ropes = _uiState.value.ropes
            ropeRepository.upsertRopes(ropes)

            val notes = _uiState.value.notes
            noteRepository.upsertNotes(notes)

            if (_uiState.value.deletedNotes.isNotEmpty()) {
                noteRepository.deleteSelectedNotes(_uiState.value.deletedNotes)
            }

            if (_uiState.value.deletedRopes.isNotEmpty()) {
                ropeRepository.deleteSelectedRopes(_uiState.value.deletedRopes)
            }
        }
    }

    fun connectNoteWithRope(target: NoteCard) {
        val source = _uiState.value.sourceNote

        if (source != null) {
            val rope = Rope(
                ropeId = UUID.randomUUID().toString(),
                sourceNoteId = source.noteId,
                targetNoteId = target.noteId,
                boardId = boardId ?: currentBoardId,
                sourceX = source.posX,
                sourceY = source.posY,
                targetX = target.posX,
                targetY = target.posY,
                targetSize = target.size,
                sourceSize = source.size
            )

            val ropes = _uiState.value.ropes.toMutableList()
            ropes.add(rope)
            _uiState.update {
                it.copy(ropes = ropes, sourceNote = null)
            }
        }
    }

    fun updateSourceNote(note: NoteCard) {
        val n = _uiState.value.notes.first { it.noteId == note.noteId }
        _uiState.update { it.copy(sourceNote = n) }
    }

    private fun updateRopePosition(note: NoteCard) {
        val ropes = _uiState.value.ropes

        if (ropes.isNotEmpty()) {
            val sourceRope = ropes.filter { it.sourceNoteId == note.noteId }
            if (sourceRope.isNotEmpty()) {
                sourceRope.forEach { rope ->
                    val r = rope.copy(sourceX = note.posX, sourceY = note.posY)
                    val selected = ropes.first { it.ropeId == rope.ropeId }
                    val index = ropes.indexOf(selected)
                    val rs = _uiState.value.ropes.toMutableList()
                    rs[index] = r
                    _uiState.update { it.copy(ropes = rs) }
                }
            }

            val targetRope = ropes.filter { it.targetNoteId == note.noteId }
            if (targetRope.isNotEmpty()) {
                targetRope.forEach { rope ->
                    val r = rope.copy(targetX = note.posX, targetY = note.posY)
                    val selected = ropes.first { it.ropeId == rope.ropeId }
                    val index = ropes.indexOf(selected)
                    val rs = _uiState.value.ropes.toMutableList()
                    rs[index] = r
                    _uiState.update { it.copy(ropes = rs) }
                }
            }
        }
    }

    fun toggleSelectionModel() {
        val isSelection = _uiState.value.isSelectionMode
        _uiState.update { it.copy(isSelectionMode = !isSelection, isConnectionMode = false) }
        if (isSelection) {
            resetSelectedNotes()
            val noteIds = _uiState.value.selectedNoteIds.toMutableList()
            noteIds.clear()
            _uiState.update { it.copy(selectedNoteIds = noteIds) }
        }
    }

    fun changeNoteSelectionStatus(noteId: String) {
        val note = _uiState.value.notes.first { it.noteId == noteId }
        val isSelected = note.isSelected
        val newNote = note.copy(isSelected = !isSelected)
        val notes = _uiState.value.notes.toMutableList()
        notes[notes.indexOf(note)] = newNote

        val noteIds = _uiState.value.selectedNoteIds.toMutableList()
        if (noteIds.contains(noteId)) {
            noteIds.remove(noteId)
        } else {
            noteIds.add(noteId)
        }

        _uiState.update { it.copy(notes = notes, selectedNoteIds = noteIds) }
    }

    fun clearNoteIds() {
        val noteIds = _uiState.value.selectedNoteIds.toMutableList()
        noteIds.clear()

        val notes = _uiState.value.notes.toMutableList()
        notes.forEach {
            val newNote = it.copy(isSelected = false)
            notes[notes.indexOf(it)] = newNote
        }

        _uiState.update { it.copy(selectedNoteIds = noteIds) }
    }

    fun deleteSelectedNotes() {
        val notes = _uiState.value.notes.toMutableList()
        val ropes = _uiState.value.ropes.toMutableList()
        val selectedNoteIds = _uiState.value.selectedNoteIds

        val deletedNotes = _uiState.value.notes.filter { selectedNoteIds.contains(it.noteId) }
        val deletedRopes = _uiState.value.ropes.filter {
            selectedNoteIds.contains(it.sourceNoteId) || selectedNoteIds.contains(it.targetNoteId)
        }

        notes.removeAll { selectedNoteIds.contains(it.noteId) }
        ropes.removeAll { selectedNoteIds.contains(it.sourceNoteId) || selectedNoteIds.contains(it.targetNoteId) }


        _uiState.update {
            it.copy(
                notes = notes,
                ropes = ropes,
                deletedNotes = deletedNotes,
                deletedRopes = deletedRopes
            )
        }

        clearNoteIds()
    }

    fun toggleConnectionMode() {
        val isConnectionMode = _uiState.value.isConnectionMode
        _uiState.update { it.copy(isConnectionMode = !isConnectionMode, isSelectionMode = false) }
        if (isConnectionMode) resetSelectedNotes()
    }

    fun noteConnectMode(noteId: String) {
        val notes = _uiState.value.notes

        val source = _uiState.value.sourceNote
        if (source == null) {
            val note = notes.first { it.noteId == noteId }.copy(isSelected = true)
            val notes = _uiState.value.notes.toMutableList()
            notes[notes.indexOfFirst { it.noteId == note.noteId }] = note
            _uiState.update { it.copy(sourceNote = note, notes = notes) }
        } else {
            if (noteId == source.noteId) {
                _uiState.update { it.copy(errorConnectSameNote = true) }
            } else {
                val note = notes.first { it.noteId == noteId }
                connectNoteWithRope(note)
                _uiState.update { it.copy(sourceNote = null) }
                resetSelectedNotes()
            }
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(errorConnectSameNote = false, successSaveBoard = false)
        }
    }

    fun loadQuotes() {
        viewModelScope.launch {
            val quotes = quoteRepository.loadQuotes()
            _uiState.update { it.copy(quotes = quotes) }
        }
    }

    fun importQuotes() {
        val quotes = _uiState.value.quotes
        quotes.forEachIndexed { i, q ->
            val space = (i + 1) * 250f
            addNote(q.quote, q.color, posX = space)
        }
    }

    fun resetSelectedNotes() {
        val notes = _uiState.value.notes.toMutableList()
        notes.forEachIndexed { index, note ->
            val newNote = note.copy(isSelected = false)
            notes[index] = newNote
        }
        _uiState.update {
            it.copy(notes = notes)
        }
    }
}