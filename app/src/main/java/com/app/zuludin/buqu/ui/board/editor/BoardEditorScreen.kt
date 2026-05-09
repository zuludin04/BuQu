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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.colors
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.ImagePickDialog
import com.app.zuludin.buqu.core.compose.saveImageToInternalStorage
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.utils.convertPathFileToUri
import com.app.zuludin.buqu.domain.models.Camera
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
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
    val context = LocalContext.current

    var showConnectionSheet by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }
    var showUpdateNoteImage by remember { mutableStateOf(false) }
    var showBoardNameDialog by remember { mutableStateOf(false) }
    var showImportQuotesDialog by remember { mutableStateOf(false) }
    var showImportBooksDialog by remember { mutableStateOf(false) }
    var isUpdateNote by remember { mutableStateOf(false) }

    var camera by remember { mutableStateOf(Camera()) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        val zoom = camera.zoom * zoomChange
        val offset = camera.offset + offsetChange
        val newCamera = camera.copy(zoom = zoom, offset = offset)
        camera = newCamera
    }

    var note by remember { mutableStateOf<NoteCard?>(null) }

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
                                viewModel.clearNoteIds()
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
                                    showBoardNameDialog = true
                                } else {
                                    viewModel.saveBoardAndCards(uiState.board?.name ?: "Board")
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
                        image = "",
                        isConnected = false
                    )
                    note = n
                    showAddNoteSheet = true
                },
                onAddNote = { showAddNoteSheet = true },
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
                GridBackgroundComponent(scale = camera.zoom, offset = camera.offset)
            }

            BoardEditor(
                notes = uiState.notes,
                ropes = uiState.ropes,
                onDragNote = { note, current -> viewModel.dragNoteCard(note, current) },
                scale = camera.zoom,
                offset = camera.offset,
                onSelectedCard = { viewModel.changeNoteSelectionStatus(it) },
                onConnectCard = { note ->
                    viewModel.updateSourceNote(note)
                    showConnectionSheet = true
                },
                onGetSize = { size, index ->
                    viewModel.getCardSize(size, index)
                },
                onUpdateNote = {
                    viewModel.toggleUpdateNote(it)
                },
                onChangeContent = { noteId, content ->
                    viewModel.updateNoteContent(noteId, content)
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
                onImportQuotes = { showImportQuotesDialog = true },
                onImportBooks = { showImportBooksDialog = true },
            )
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.errorConnectSameNote) {
            scaffoldState.snackbarHostState.showSnackbar("Can't connect to same note")
            viewModel.snackbarMessageShown()
        }

        if (uiState.successSaveBoard) {
            scaffoldState.snackbarHostState.showSnackbar("Your board is saved")
            viewModel.snackbarMessageShown()
        }
    }

    if (showBoardNameDialog) {
        BoardNameDialog(
            onDismiss = { showBoardNameDialog = !showBoardNameDialog },
            onConfirm = { name, color ->
                viewModel.saveBoardAndCards(name, color)
                showBoardNameDialog = !showBoardNameDialog
            },
        )
    }

    if (showConnectionSheet) {
        NoteConnectDialog(
            source = uiState.sourceNote!!,
            notes = uiState.notes,
            onDismiss = {
                showConnectionSheet = !showConnectionSheet
                if (it != null) {
                    val note = uiState.notes.first { n -> n.noteId == it.noteId }
                    viewModel.connectNoteWithRope(note)
                }
            },
        )
    }

    if (showAddNoteSheet) {
        NoteInputDialog(
            onDismiss = {
                showAddNoteSheet = !showAddNoteSheet
                isUpdateNote = !isUpdateNote
            },
            note = note,
            isUpdate = isUpdateNote,
            onConfirm = { content, color ->
                if (!isUpdateNote) {
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
                        title = content,
                        image = "",
                        color = color,
                        posX = (rx - camera.offset.x) / camera.zoom,
                        posY = (ry - camera.offset.y) / camera.zoom
                    )
                } else {
                    viewModel.updateNote(
                        noteId = note!!.noteId, text = content, image = "", color = color
                    )
                    isUpdateNote = false
                    note = null
                }
                showAddNoteSheet = !showAddNoteSheet
            },
        )
    }

    if (showImportQuotesDialog) {
        QuoteImportDialog(
            quotes = uiState.quotes,
            onDismiss = { showImportQuotesDialog = !showImportQuotesDialog },
            onQuoteSelected = { viewModel.selectImportQuote(it.quoteId) },
            onImportQuotes = {
                viewModel.importQuotes()
                showImportQuotesDialog = !showImportQuotesDialog
            },
            categories = uiState.categories
        )
    }

    if (showImportBooksDialog) {
        BookImportDialog(
            books = uiState.books,
            onDismiss = { showImportBooksDialog = !showImportBooksDialog },
            onBookSelected = { viewModel.selectImportBook(it.bookId) },
            onImportBooks = {
                viewModel.importBooks()
                showImportBooksDialog = !showImportBooksDialog
            },
        )
    }

    if (showUpdateNoteImage) {
        ImagePickDialog(
            uri = note!!.image.convertPathFileToUri(),
            onDismiss = {
                showUpdateNoteImage = !showUpdateNoteImage
                isUpdateNote = false
                note = null
            },
            showScanText = false,
            color = note!!.color,
            onSaveImage = { selectedColor, uri ->
                val path = saveImageToInternalStorage(context, uri)
                if (path != null) {
                    viewModel.updateNote(note!!.noteId, "", path, selectedColor)
                }
                showUpdateNoteImage = !showUpdateNoteImage
                isUpdateNote = false
                note = null
            },
            onScanText = { },
        )
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
    onConnectCard: (NoteCard) -> Unit,
    onSelectedCard: (String) -> Unit,
    onUpdateNote: (String) -> Unit,
    onChangeContent: (String, String) -> Unit,
    onAddQuickNote: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    noteHighlightedId: String?,
    previewRope: Rope?,
) {
    var showMenu by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedNote by remember { mutableStateOf<NoteCard?>(null) }

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
                onPositionChanged = { note ->
                    val offset = Offset(
                        x = note.posX + note.size.width / 2f,
                        y = note.posY + note.size.height / 2f
                    )

                    onDragNote(note, offset)
                },
                onGetSize = { size -> onGetSize(size, index) },
                onSelect = { t -> onSelectedCard(t.noteId) },
                onPopupMenu = { off ->
                    popupOffset = off
                    showMenu = true
                },
                onUpdateNote = onUpdateNote,
                onChangeContent = onChangeContent,
                onDragEnd = onDragEnd,
                scale = scale,
                isHighlighted = n.noteId == noteHighlightedId
            )
        }

        if (showMenu) {
            val screenOffset = Offset(
                popupOffset.x * scale + offset.x, popupOffset.y * scale + offset.y
            )
            Popup(
                offset = IntOffset(screenOffset.x.roundToInt(), screenOffset.y.roundToInt()),
                onDismissRequest = { showMenu = false },
                content = {
                    Column(modifier = Modifier.widthIn(max = 150.dp)) {
                        OverflowMenuItem(
                            title = "Connect Note",
                            onClick = {
                                onConnectCard(selectedNote!!)
                                showMenu = false
                            },
                        )
                        OverflowMenuItem(
                            title = "Update Note",
                            onClick = {},
                        )
                    }
                },
            )
        }
    }
}