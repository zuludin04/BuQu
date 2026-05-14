package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.BoardEngine
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.usecase.board.GetBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.UpsertBoardUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BoardEditorViewModel @Inject constructor(
    private val getBoard: GetBoardUseCase,
    private val upsertBoard: UpsertBoardUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val boardId: String? = savedStateHandle[BuquDestinationArgs.BOARD_ID_ARG]
    private val engine = BoardEngine()

    private val _uiState = MutableStateFlow(BoardEditorState())
    val uiState: StateFlow<BoardEditorState> = _uiState

    private val _eventChannel = Channel<BoardEditorEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var currentBoardId: String

    init {
        loadData(boardId)
        currentBoardId = UUID.randomUUID().toString()
    }

    fun loadData(boardId: String?) {
        viewModelScope.launch {
            getBoard.invoke(boardId).let { data ->
                _uiState.update {
                    it.copy(
                        board = data.board,
                        notes = data.notes,
                        ropes = data.ropes,
                        quotes = data.quotes,
                        books = data.books
                    )
                }
            }
        }
    }

    fun dragNoteCard(note: NoteCard, current: Offset) {
        val result = engine.drag(note, _uiState.value.notes, _uiState.value.ropes, current)
        val previewRope = createPreviewRope(note, result.nearestNote)

        _uiState.update {
            it.copy(
                notes = result.notes,
                ropes = result.ropes,
                noteHighlightId = result.nearestNote?.noteId,
                previewRope = previewRope
            )
        }
    }

    fun onDragEnd() {
        val ropes = _uiState.value.ropes.toMutableList()
        val rope = _uiState.value.previewRope

        if (rope != null) {
            ropes.add(rope)
            _uiState.update { it.copy(ropes = ropes) }
        }

        _uiState.update { it.copy(noteHighlightId = null, previewRope = null) }
    }

    fun tidyUpNotes(boardWidth: Float, boardHeight: Float) {
        val notes = _uiState.value.notes
        val ropes = _uiState.value.ropes
        val tidiedResult = engine.tidyUpNotes(notes, ropes, boardWidth, boardHeight)

        _uiState.update { it.copy(notes = tidiedResult.notes, ropes = tidiedResult.ropes) }
    }

    fun toggleGrid() {
        val isShown = _uiState.value.showGrid
        _uiState.update { it.copy(showGrid = !isShown) }
    }

    fun getCardSize(size: IntSize, index: Int) {
        val notes = _uiState.value.notes.mapIndexed { i, card ->
            if (index == i) {
                card.copy(size = size)
            } else {
                card
            }
        }
        _uiState.update { it.copy(notes = notes) }
    }

    fun addNote(
        title: String,
        image: String,
        color: String,
        posX: Float? = null,
        posY: Float? = null,
        isQuickAdd: Boolean = false
    ) {
        val randomX = (100..600).random().toFloat()
        val randomY = (100..1000).random().toFloat()

        val note = NoteCard(
            noteId = UUID.randomUUID().toString(),
            boardId = boardId ?: currentBoardId,
            title = title,
            posX = posX ?: randomX,
            posY = posY ?: randomY,
            color = color,
            size = IntSize.Zero,
            image = image,
            isUpdate = isQuickAdd
        )
        val notes = _uiState.value.notes.toMutableList()
        notes.add(note)
        _uiState.update {
            it.copy(notes = notes)
        }
    }

    fun updateNote(noteId: String, text: String, image: String, color: String) {
        val notes = _uiState.value.notes.toMutableList()
        val note = notes.first { it.noteId == noteId }.copy(
            title = text,
            image = image,
            color = color
        )
        notes[notes.indexOfFirst { it.noteId == noteId }] = note
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveBoardAndCards(name: String, color: String = "000000") {
        viewModelScope.launch {
            val board = Board(boardId ?: currentBoardId, name, color)
            val notes = _uiState.value.notes
            val ropes = _uiState.value.ropes

            upsertBoard.invoke(board, notes, ropes)
            _eventChannel.send(BoardEditorEvent.SuccessSaveBoard)
        }
    }

    fun changeNoteSelectionStatus(noteId: String) {
        val notes = _uiState.value.notes.map {
            if (it.noteId == noteId) {
                val isSelected = it.isSelected
                it.copy(isSelected = !isSelected)
            } else {
                it
            }
        }

        val noteIds = _uiState.value.selectedNoteIds.toMutableList()
        if (noteIds.contains(noteId)) {
            noteIds.remove(noteId)
        } else {
            noteIds.add(noteId)
        }

        _uiState.update { it.copy(notes = notes, selectedNoteIds = noteIds) }
    }

    fun deleteSelectedNotes() {
        val selectedNoteIds = _uiState.value.selectedNoteIds

        val notes = _uiState.value.notes.map {
            if (selectedNoteIds.contains(it.noteId)) {
                it.copy(status = "deleted", isSelected = false)
            } else {
                it.copy(isSelected = false)
            }
        }
        val ropes = _uiState.value.ropes.map {
            if (selectedNoteIds.contains(it.sourceNoteId) || selectedNoteIds.contains(it.targetNoteId)) {
                it.copy(status = "deleted")
            } else {
                it
            }
        }

        _uiState.update {
            it.copy(notes = notes, ropes = ropes, selectedNoteIds = emptyList())
        }
    }

    fun resetSelectedNotes() {
        val notes = _uiState.value.notes.map { note ->
            note.copy(isSelected = false)
        }
        _uiState.update {
            it.copy(notes = notes, selectedNoteIds = emptyList())
        }
    }

    fun importQuotes() {
        val selectedQuotes = _uiState.value.quotes.filter { it.isSelected }
        selectedQuotes.forEachIndexed { i, q ->
            val space = (i + 1) * 250f
            addNote(q.quote, q.image, q.color, posX = space)
        }

        val quotes = _uiState.value.quotes.map { it.copy(isSelected = false) }
        _uiState.update { it.copy(quotes = quotes) }
    }

    fun importBooks() {
        val selectedBooks = _uiState.value.books.filter { it.isSelected }
        selectedBooks.forEachIndexed { i, b ->
            val space = (i + 1) * 250f
            addNote(
                title = b.title,
                image = "",
                color = "E1F5FE",
                posX = space
            )
        }

        val books = _uiState.value.books.map { it.copy(isSelected = false) }
        _uiState.update { it.copy(books = books) }
    }

    fun selectImportQuote(quoteId: String) {
        val quotes = _uiState.value.quotes.map {
            if (it.quoteId == quoteId) it.copy(isSelected = !it.isSelected) else it
        }
        _uiState.update { it.copy(quotes = quotes) }
    }

    fun selectImportBook(bookId: String) {
        val books = _uiState.value.books.map {
            if (it.bookId == bookId) it.copy(isSelected = !it.isSelected) else it
        }
        _uiState.update { it.copy(books = books) }
    }

    private fun createPreviewRope(source: NoteCard, target: NoteCard?): Rope? {
        if (target != null) {
            return Rope(
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
        } else {
            return null
        }
    }
}