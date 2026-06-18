package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.zuludin.buqu.core.utils.BoardEngine
import com.app.zuludin.buqu.domain.models.Board
import com.app.zuludin.buqu.domain.models.Book
import com.app.zuludin.buqu.domain.models.Camera
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.NoteType
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.domain.usecase.board.DeleteBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.GetBoardUseCase
import com.app.zuludin.buqu.domain.usecase.board.UpsertBoardUseCase
import com.app.zuludin.buqu.navigation.BuquDestinationArgs
import com.app.zuludin.buqu.ui.board.editor.BoardDialogState.UpsertBoardDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.math.min
import kotlin.random.Random

@HiltViewModel
class BoardEditorViewModel @Inject constructor(
    private val getBoard: GetBoardUseCase,
    private val upsertBoard: UpsertBoardUseCase,
    private val deleteBoard: DeleteBoardUseCase,
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

    fun onAction(action: BoardEditorAction) {
        when (action) {
            is BoardEditorAction.OnOpenDialog -> {
                _uiState.update { it.copy(dialogState = action.state) }
            }

            is BoardEditorAction.ConfirmInputNote -> upsertNote(
                action.noteId, action.content, action.image, action.color
            )

            is BoardEditorAction.TransformCamera -> updateCamera(action.offset, action.zoom)
            is BoardEditorAction.DragNote -> dragNoteCard(action.noteCard, action.offset)
            is BoardEditorAction.OnSelectNote -> changeNoteSelectionStatus(action.noteId)
            BoardEditorAction.DeleteBoard -> deleteBoard()
            BoardEditorAction.OnDeleteSelectedNotes -> deleteSelectedNotes()
            BoardEditorAction.OnResetSelectedNotes -> resetSelectedNotes()
            BoardEditorAction.OnTidyUpNotes -> tidyUpNotes()
            BoardEditorAction.OnToggleGrid -> toggleGrid()
            BoardEditorAction.OnCheckBoard -> {
                val board = _uiState.value.board
                if (board != null) {
                    upsertBoardAndCards(board.name, board.color)
                } else {
                    _uiState.update { it.copy(dialogState = UpsertBoardDialog()) }
                }
            }

            is BoardEditorAction.ConfirmUpsertBoard -> upsertBoardAndCards(
                action.name, action.color
            )

            is BoardEditorAction.OnGetBoardSize -> {
                _uiState.update { it.copy(boardSize = action.size) }
                zoomToFit()
            }

            is BoardEditorAction.OnGetNoteSize -> getCardSize(action.size, action.index)
            is BoardEditorAction.ConfirmImportBooks -> importBooks(action.books)
            is BoardEditorAction.ConfirmImportQuotes -> importQuotes(action.quotes)
            BoardEditorAction.OnDragEnd -> onDragEnd()
            is BoardEditorAction.OnConfirmConnectNote -> confirmConnectNote(
                action.source, action.target
            )

            is BoardEditorAction.OnCanvasTap -> handleCanvasTap(action.offset)
            is BoardEditorAction.OnDeleteRope -> deleteRope(action.ropeId)
            is BoardEditorAction.OnUpdateRope -> updateRope(
                action.ropeId, action.text, action.color
            )

            is BoardEditorAction.OnCreatePreviewRope -> {
                val note = _uiState.value.notes.first { it.noteId == action.handler.noteId }
                val previewRope = _uiState.value.previewRope
                if (previewRope == null) {
                    val rope = Rope(
                        ropeId = UUID.randomUUID().toString(),
                        sourceNoteId = note.noteId,
                        targetNoteId = "",
                        boardId = boardId ?: currentBoardId,
                        sourceX = note.posX,
                        sourceY = note.posY,
                        targetX = action.handler.position.x,
                        targetY = action.handler.position.y,
                        targetSize = IntSize.Zero,
                        sourceSize = note.size
                    )
                    _uiState.update {
                        it.copy(
                            previewRope = rope,
                            dialogState = BoardDialogState.None,
                            selectedRopeId = null,
                        )
                    }
                } else {
                    val targetX = previewRope.targetX + action.dragAmount.x
                    val targetY = previewRope.targetY + action.dragAmount.y

                    val targetNote = engine.findNote(Offset(targetX, targetY), _uiState.value.notes)

                    _uiState.update {
                        it.copy(
                            previewRope = previewRope.copy(
                                targetX = targetNote?.posX ?: targetX,
                                targetY = targetNote?.posY ?: targetY,
                                targetNoteId = targetNote?.noteId ?: "",
                                targetSize = targetNote?.size ?: IntSize.Zero
                            )
                        )
                    }
                }
            }

            BoardEditorAction.OnDragArrowEnd -> {
                val previewRope = _uiState.value.previewRope
                if (previewRope != null && previewRope.targetNoteId != "") {
                    val connectedRopes =
                        _uiState.value.ropes.filter { (previewRope.sourceNoteId == it.sourceNoteId || previewRope.sourceNoteId == it.targetNoteId) && (previewRope.targetNoteId == it.sourceNoteId || previewRope.targetNoteId == it.targetNoteId) }
                    if (connectedRopes.isNotEmpty()) {
                        viewModelScope.launch { _eventChannel.send(BoardEditorEvent.NoteAlreadyConnected) }
                    } else {
                        createConnectedRope(previewRope)
                    }
                }
                _uiState.update { it.copy(previewRope = null, selectedNoteIds = emptyList()) }
            }
        }
    }

    private fun loadData(boardId: String?) {
        viewModelScope.launch {
            getBoard.invoke(boardId).let { data ->
                _uiState.update {
                    it.copy(
                        board = data.board,
                        notes = data.notes,
                        ropes = data.ropes,
                        quotes = data.quotes,
                        books = data.books,
                    )
                }
            }
        }
    }

    private fun deleteBoard() {
        viewModelScope.launch {
            deleteBoard.invoke(boardId!!)
            _eventChannel.send(BoardEditorEvent.GoHome)
        }
    }

    private fun upsertNote(
        noteId: String?,
        title: String,
        image: String,
        color: String,
        posX: Float? = null,
        posY: Float? = null,
        isQuickAdd: Boolean = false,
        type: NoteType? = null
    ) {
        if (noteId == null) {
            val boardSize = _uiState.value.boardSize
            val camera = _uiState.value.camera

            val random = Random.Default
            val minX = boardSize.width * 0.2f
            val maxX = boardSize.width * 0.6f
            val minY = boardSize.height * 0.2f
            val maxY = boardSize.height * 0.6f

            val rx = if (maxX > minX) random.nextDouble(minX.toDouble(), maxX.toDouble())
                .toFloat() else minX
            val ry = if (maxY > minY) random.nextDouble(minY.toDouble(), maxY.toDouble())
                .toFloat() else minY

            val worldPos = camera.screenToWorld(Offset(posX ?: rx, posY ?: ry))

            val calculatedType = type ?: if (image.isNotEmpty()) NoteType.Image else NoteType.Text

            val note = NoteCard(
                noteId = UUID.randomUUID().toString(),
                boardId = boardId ?: currentBoardId,
                title = title,
                posX = worldPos.x,
                posY = worldPos.y,
                color = color,
                size = IntSize.Zero,
                image = image,
                isUpdate = isQuickAdd,
                type = calculatedType
            )

            _uiState.update {
                it.copy(notes = it.notes + note, dialogState = BoardDialogState.None)
            }
        } else {
            val notes = _uiState.value.notes.map {
                if (it.noteId == noteId) it.copy(title = title, color = color) else it
            }
            _uiState.update { it.copy(notes = notes, dialogState = BoardDialogState.None) }
        }
    }

    private fun updateCamera(pan: Offset, zoom: Float) {
        val camera = _uiState.value.camera
        val new = camera.copy(offset = pan, zoom = zoom)
        _uiState.update { it.copy(camera = new) }
    }

    private fun dragNoteCard(note: NoteCard, current: Offset) {
        val result = engine.drag(note, current, _uiState.value)
        _uiState.value = result
    }

    private fun changeNoteSelectionStatus(noteId: String) {
        val notes = _uiState.value.notes.map {
            if (it.noteId == noteId) {
                val isSelected = it.isSelected
                it.copy(isSelected = !isSelected)
            } else {
                it
            }
        }

        val selectedNoteIds = _uiState.value.selectedNoteIds
        val noteIds =
            if (selectedNoteIds.contains(noteId)) selectedNoteIds - noteId else selectedNoteIds + noteId

        _uiState.update {
            it.copy(notes = notes, selectedNoteIds = noteIds)
        }
    }

    private fun deleteSelectedNotes() {
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

    private fun resetSelectedNotes() {
        val notes = _uiState.value.notes.map { note ->
            note.copy(isSelected = false)
        }
        _uiState.update {
            it.copy(notes = notes, selectedNoteIds = emptyList())
        }
    }

    private fun tidyUpNotes() {
        _uiState.value = engine.tidyUpNotes(_uiState.value)
    }

    private fun toggleGrid() {
        val isShown = _uiState.value.showGrid
        _uiState.update { it.copy(showGrid = !isShown, dialogState = BoardDialogState.None) }
    }

    fun createConnectedRope(rope: Rope) {
        _uiState.update { it.copy(ropes = it.ropes + rope) }
    }

    private fun upsertBoardAndCards(name: String, color: String) {
        viewModelScope.launch {
            val board = Board(boardId ?: currentBoardId, name, color)
            val notes = _uiState.value.notes
            val ropes = _uiState.value.ropes

            upsertBoard.invoke(board, notes, ropes)
            _eventChannel.send(BoardEditorEvent.SuccessSaveBoard)
            _uiState.update { it.copy(board = board, dialogState = BoardDialogState.None) }
        }
    }

    private fun getCardSize(size: IntSize, index: Int) {
        val notes = _uiState.value.notes.mapIndexed { i, card ->
            if (index == i) {
                card.copy(size = size)
            } else {
                card
            }
        }
        _uiState.update { it.copy(notes = notes) }
    }

    private fun importQuotes(quotes: List<Quote>) {
        quotes.forEachIndexed { i, q ->
            val space = (i + 1) * 250f
            upsertNote(
                null,
                "${q.quote}-${q.author}",
                q.image,
                q.color,
                posX = space,
                type = NoteType.Quote
            )
        }

        _uiState.update { it.copy(dialogState = BoardDialogState.None) }
    }

    private fun importBooks(books: List<Book>) {
        books.forEachIndexed { i, b ->
            val space = (i + 1) * 250f
            upsertNote(
                noteId = null,
                title = "${b.author}-${b.title}",
                image = b.cover,
                color = "E1F5FE",
                posX = space,
                type = NoteType.Book
            )
        }

        _uiState.update { it.copy(dialogState = BoardDialogState.None) }
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

    private fun onDragEnd() {
        val rope = _uiState.value.previewRope

        if (rope != null) {
            viewModelScope.launch {
                _eventChannel.send(BoardEditorEvent.CreateConnectedRope(rope))
            }
        }

        _uiState.update { it.copy(noteHighlightId = null, previewRope = null) }
    }

    private fun confirmConnectNote(source: NoteCard, target: NoteCard?) {
        val rope = createPreviewRope(source, target)
        val connectedRopes =
            _uiState.value.ropes.filter { (source.noteId == it.sourceNoteId || source.noteId == it.targetNoteId) && (target?.noteId == it.sourceNoteId || target?.noteId == it.targetNoteId) }
        if (connectedRopes.isNotEmpty()) {
            viewModelScope.launch { _eventChannel.send(BoardEditorEvent.NoteAlreadyConnected) }
        } else {
            createConnectedRope(rope!!)
        }
        _uiState.update { it.copy(dialogState = BoardDialogState.None) }
    }

    private fun handleCanvasTap(offset: Offset) {
        val camera = _uiState.value.camera
        val worldTap = camera.screenToWorld(offset)
        _uiState.value = engine.onTap(worldTap, boardId ?: currentBoardId, _uiState.value)
    }

    private fun deleteRope(ropeId: String) {
        val ropes = _uiState.value.ropes.map {
            if (it.ropeId == ropeId) it.copy(status = "deleted") else it
        }
        _uiState.update { it.copy(ropes = ropes, dialogState = BoardDialogState.None) }
    }

    private fun updateRope(ropeId: String, caption: String, color: String) {
        val ropes = _uiState.value.ropes.map {
            if (it.ropeId == ropeId) it.copy(color = color, caption = caption) else it
        }
        _uiState.update {
            it.copy(
                dialogState = BoardDialogState.None, ropes = ropes, selectedRopeId = null
            )
        }
    }

    private fun zoomToFit() {
        if (_uiState.value.initializedCamera) return

        val notes = _uiState.value.notes
        if (notes.isEmpty()) return

        if (_uiState.value.boardSize == IntSize.Zero) return

        val size = _uiState.value.boardSize

        val minX = notes.minOfOrNull { it.posX } ?: 0f
        val minY = notes.minOfOrNull { it.posY } ?: 0f
        val maxX = notes.maxOfOrNull { it.posX + it.size.width } ?: 0f
        val maxY = notes.maxOfOrNull { it.posY + it.size.height } ?: 0f

        val boardWidth = maxX - minX
        val boardHeight = maxY - minY

        val zoomX = size.width / boardWidth
        val zoomY = size.height / boardHeight

        val zoom = min(zoomX, zoomY)

        val centerX = (minX + maxX) / 2f
        val centerY = (minY + maxY) / 2f

        val offsetX = size.width / 2f - centerX * zoom
        val offsetY = size.height / 2f - centerY * zoom

        val fitCamera = Camera(zoom, Offset(offsetX, offsetY))
        _uiState.update { it.copy(initializedCamera = true) }
    }
}