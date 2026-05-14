package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.colors
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.domain.models.Camera
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.DismissDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnImportBooks
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnImportQuotes
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenBookDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenNewBoardDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenQuoteDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnSaveBoard
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun BoardEditorScreen(
    viewModel: BoardEditorViewModel = hiltViewModel(),
    topAppBarTitle: String,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var camera by remember { mutableStateOf(Camera()) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        val zoom = camera.zoom * zoomChange
        val offset = camera.offset + offsetChange
        val newCamera = camera.copy(zoom = zoom, offset = offset)
        camera = newCamera
    }

    var boardSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = if (uiState.selectedNoteIds.isNotEmpty()) "${uiState.selectedNoteIds.size} Selected" else (uiState.board?.name
                    ?: topAppBarTitle),
                backButton = {
                    IconButton(
                        onClick = {
                            if (uiState.selectedNoteIds.isNotEmpty()) {
                                viewModel.resetSelectedNotes()
                            } else {
                                onBack()
                            }
                        },
                        content = {
                            Icon(
                                if (uiState.selectedNoteIds.isNotEmpty()) PhosphorX else PhosphorArrowLeft,
                                null
                            )
                        },
                    )
                },
                actions = {
                    if (uiState.selectedNoteIds.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.deleteSelectedNotes() },
                            content = { Icon(PhosphorTrash, null) },
                        )
                    } else {
                        IconButton(
                            onClick = {
                                if (uiState.board == null) {
                                    viewModel.onAction(OnOpenNewBoardDialog)
                                } else {
                                    viewModel.onAction(
                                        OnSaveBoard(
                                            uiState.board?.name ?: "Board",
                                            uiState.board?.color ?: "000000"
                                        )
                                    )
                                }
                            },
                            content = { Icon(PhosphorCheck, null) },
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomBarEditor(
                onTextResult = { text ->
                    val n = NoteCard(
                        noteId = "",
                        boardId = "",
                        title = text,
                        posX = 0f,
                        posY = 0f,
                        size = IntSize.Zero,
                        isSelected = false,
                        color = "",
                        image = ""
                    )
                    viewModel.onAction(BoardEditorAction.OnOpenAddNoteDialog(n, false))
                },
                onAddNote = {
                    viewModel.onAction(
                        BoardEditorAction.OnOpenAddNoteDialog(
                            null,
                            false
                        )
                    )
                },
                onSaveImage = { path, color ->
                    val random = Random.Default
                    val minX = boardSize.width * 0.2f
                    val maxX = boardSize.width * 0.6f
                    val minY = boardSize.height * 0.2f
                    val maxY = boardSize.height * 0.6f

                    val rx = if (maxX > minX) random.nextDouble(minX.toDouble(), maxX.toDouble())
                        .toFloat() else minX
                    val ry = if (maxY > minY) random.nextDouble(minY.toDouble(), maxY.toDouble())
                        .toFloat() else minY

                    viewModel.addNote(
                        title = "",
                        image = path,
                        color = color,
                        posX = (rx - camera.offset.x) / camera.zoom,
                        posY = (ry - camera.offset.y) / camera.zoom
                    )
                },
                onTidyUp = {
                    viewModel.tidyUpNotes(
                        boardSize.width.toFloat(), boardSize.height.toFloat()
                    )
                },
                onToggleGrid = { viewModel.toggleGrid() },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onSizeChanged { boardSize = it }
                .transformable(state)) {
            if (uiState.showGrid) {
                DotBackgroundComponent(scale = camera.zoom, offset = camera.offset)
            }

            BoardEditor(
                notes = uiState.notes.filter { it.status == "active" },
                ropes = uiState.ropes.filter { it.status == "active" },
                onDragNote = { note, current -> viewModel.dragNoteCard(note, current) },
                scale = camera.zoom,
                offset = camera.offset,
                onSelectedCard = { viewModel.changeNoteSelectionStatus(it) },
                onGetSize = { size, index ->
                    viewModel.getCardSize(size, index)
                },
                onAddQuickNote = {
                    viewModel.addNote(
                        title = "",
                        image = "",
                        color = colors[0],
                        posX = (it.x - camera.offset.x) / camera.zoom,
                        posY = (it.y - camera.offset.y) / camera.zoom,
                        isQuickAdd = true
                    )
                },
                onDragEnd = { viewModel.onDragEnd() },
                noteHighlightedId = uiState.noteHighlightId,
                previewRope = uiState.previewRope
            )

            BoardTools(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onZoomIn = {
                    val scale = (camera.zoom - 0.1f).coerceAtLeast(0.5f)
                    val newZoomCamera = camera.copy(zoom = scale)
                    camera = newZoomCamera
                },
                onZoomOut = {
                    val scale = (camera.zoom + 0.1f).coerceAtMost(3f)
                    val newZoomCamera = camera.copy(zoom = scale)
                    camera = newZoomCamera
                },
                onResetZoom = {
                    camera = Camera()
                },
                scale = camera.zoom,
                onImportQuotes = { viewModel.onAction(OnOpenQuoteDialog) },
                onImportBooks = { viewModel.onAction(OnOpenBookDialog) },
            )
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                BoardEditorEvent.SuccessSaveBoard -> {
                    scaffoldState.snackbarHostState.showSnackbar("Your board is saved")
                }
            }
        }
    }

    when (val dialog = uiState.dialogState) {
        BoardDialogState.None -> Unit
        BoardDialogState.ImportBooks -> {
            BookImportDialog(
                books = uiState.books,
                onDismiss = { viewModel.onAction(DismissDialog) },
                onBookSelected = { viewModel.selectImportBook(it.bookId) },
                onImportBooks = { viewModel.onAction(OnImportBooks) },
            )
        }

        BoardDialogState.ImportQuotes -> {
            QuoteImportDialog(
                quotes = uiState.quotes,
                onDismiss = { viewModel.onAction(DismissDialog) },
                onQuoteSelected = { viewModel.selectImportQuote(it.quoteId) },
                onImportQuotes = { viewModel.onAction(OnImportQuotes) },
            )
        }

        BoardDialogState.NewBoard -> {
            BoardNameDialog(
                onDismiss = { viewModel.onAction(DismissDialog) },
                onConfirm = { name, color ->
                    viewModel.onAction(OnSaveBoard(name, color))
                },
            )
        }

        is BoardDialogState.AddNote -> {
            NoteInputDialog(
                onDismiss = { viewModel.onAction(DismissDialog) },
                note = dialog.note,
                isUpdate = dialog.isUpdate,
                onConfirm = { content, color ->
                    if (!dialog.isUpdate) {
                        val random = Random.Default
                        val minX = boardSize.width * 0.2f
                        val maxX = boardSize.width * 0.6f
                        val minY = boardSize.height * 0.2f
                        val maxY = boardSize.height * 0.6f

                        val rx =
                            if (maxX > minX) random.nextDouble(minX.toDouble(), maxX.toDouble())
                                .toFloat() else minX
                        val ry =
                            if (maxY > minY) random.nextDouble(minY.toDouble(), maxY.toDouble())
                                .toFloat() else minY

                        viewModel.addNote(
                            title = content,
                            image = "",
                            color = color,
                            posX = (rx - camera.offset.x) / camera.zoom,
                            posY = (ry - camera.offset.y) / camera.zoom
                        )
                    } else {
                        viewModel.updateNote(
                            noteId = dialog.note!!.noteId, text = content, image = "", color = color
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun BoardEditor(
    modifier: Modifier = Modifier,
    notes: List<NoteCard>,
    ropes: List<Rope>,
    onDragNote: (NoteCard, Offset) -> Unit,
    onGetSize: (IntSize, Int) -> Unit,
    scale: Float,
    offset: Offset,
    onSelectedCard: (String) -> Unit,
    onAddQuickNote: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    noteHighlightedId: String?,
    previewRope: Rope?,
) {
    var showMenu by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y,
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { onAddQuickNote(it) },
                )
            },
    ) {
        ropes.forEach {
            RopeComponent(it)
        }

        if (previewRope != null) RopeComponent(previewRope)

        notes.forEachIndexed { index, n ->
            NoteCardComponent(
                note = n,
                onPositionChanged = { pos ->
                    onDragNote(n, pos)
                },
                onGetSize = { size -> onGetSize(size, index) },
                onSelect = { t -> onSelectedCard(t.noteId) },
                onPopupMenu = { off ->
                    popupOffset = off
                    showMenu = true
                },
                onDragEnd = onDragEnd,
                scale = scale,
                isHighlighted = n.noteId == noteHighlightedId
            )
        }

        if (showMenu) {
            val screenOffset = Offset(x = popupOffset.x * scale, y = popupOffset.y * scale)
            Popup(
                offset = IntOffset(screenOffset.x.roundToInt(), screenOffset.y.roundToInt()),
                onDismissRequest = { showMenu = false },
                content = {
                    Column(modifier = Modifier.widthIn(max = 150.dp)) {
                        OverflowMenuItem(
                            title = "Update Note",
                            onClick = { },
                        )
                    }
                },
            )
        }
    }
}