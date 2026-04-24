package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.TransformableState
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.ImagePickDialog
import com.app.zuludin.buqu.core.compose.saveImageToInternalStorage
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorNote
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.utils.convertPathFileToUri
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
    var isUpdateNote by remember { mutableStateOf(false) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    var note by remember { mutableStateOf<NoteCard?>(null) }

    var boardSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = if (uiState.isSelectionMode) "${uiState.selectedNoteIds.size} Selected" else (uiState.board?.name
                    ?: topAppBarTitle),
                backButton = {
                    IconButton(
                        onClick = {
                            if (uiState.isSelectionMode) {
                                viewModel.toggleSelectionModel()
                                viewModel.clearNoteIds()
                            } else {
                                onBack()
                            }
                        },
                        content = {
                            Icon(
                                if (uiState.isSelectionMode) PhosphorX else PhosphorArrowLeft, null
                            )
                        }
                    )
                }, actions = {
                    if (uiState.isSelectionMode) {
                        IconButton(
                            onClick = { viewModel.deleteSelectedNotes() },
                            content = { Icon(PhosphorTrash, null) }
                        )
                    } else {
                        IconButton(
                            onClick = { showImportQuotesDialog = true },
                            content = { Icon(PhosphorNote, null) }
                        )
                        IconButton(
                            onClick = {
                                if (uiState.board == null) {
                                    showBoardNameDialog = true
                                } else {
                                    viewModel.saveBoardAndCards(uiState.board?.name ?: "Board")
                                }
                            },
                            content = { Icon(PhosphorCheck, null) }
                        )
                    }
                }
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
                        posX = (rx - offset.x) / scale,
                        posY = (ry - offset.y) / scale
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onSizeChanged { boardSize = it }
                .border(
                    width = if (uiState.isConnectionMode || uiState.isSelectionMode) 4.dp else 0.dp,
                    color = if (uiState.isConnectionMode) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )
                    else if (uiState.isSelectionMode) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                    else Color.Transparent
                )
        ) {
            GridBackgroundComponent(scale = scale, offset = offset)

            BoardEditor(
                notes = uiState.notes,
                ropes = uiState.ropes,
                onDragNote = { noteId, x, y ->
                    viewModel.dragNoteCard(noteId, x, y)
                },
                scale = scale,
                offset = offset,
                state = state,
                isSelectionMode = uiState.isSelectionMode,
                onSelectedCard = { id ->
                    if (uiState.isConnectionMode) {
                        viewModel.noteConnectMode(id)
                    }

                    if (uiState.isSelectionMode) {
                        viewModel.changeNoteSelectionStatus(id)
                    }
                },
                isConnectionMode = uiState.isConnectionMode,
                onConnectCard = { note ->
                    viewModel.updateSourceNote(note)
                    showConnectionSheet = true
                },
                onGetSize = { size, index ->
                    viewModel.getCardSize(size, index)
                },
                onUpdateNote = {
                    note = it
                    isUpdateNote = true
                    if (it.image.isBlank()) {
                        showAddNoteSheet = true
                    } else {
                        showUpdateNoteImage = true
                    }
                }
            )

            ZoomPanTool(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                onZoomIn = { scale = (scale - 0.1f).coerceAtLeast(0.5f) },
                onZoomOut = { scale = (scale + 0.1f).coerceAtMost(3f) },
                onResetZoom = {
                    scale = 1f
                    offset = Offset.Zero
                },
                scale = scale
            )

            SelectConnectTool(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                isSelectionMode = uiState.isSelectionMode,
                isConnectionMode = uiState.isConnectionMode,
                onToggleSelectionMode = { viewModel.toggleSelectionModel() },
                onToggleConnectionMode = { viewModel.toggleConnectionMode() }
            )

            if (uiState.isConnectionMode || uiState.isSelectionMode) {
                SelectionConnectionIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    isConnectionMode = uiState.isConnectionMode
                )
            }
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
            }
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
            }
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
                        posX = (rx - offset.x) / scale,
                        posY = (ry - offset.y) / scale
                    )
                } else {
                    viewModel.updateNote(
                        noteId = note!!.noteId,
                        text = content,
                        image = "",
                        color = color
                    )
                    isUpdateNote = false
                    note = null
                }
                showAddNoteSheet = !showAddNoteSheet
            }
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
    onDragNote: (String, Float, Float) -> Unit,
    onGetSize: (IntSize, Int) -> Unit,
    scale: Float,
    offset: Offset,
    state: TransformableState,
    isSelectionMode: Boolean,
    isConnectionMode: Boolean,
    onConnectCard: (NoteCard) -> Unit,
    onSelectedCard: (String) -> Unit,
    onUpdateNote: (NoteCard) -> Unit
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
                translationY = offset.y
            )
            .transformable(state = state)
    ) {
        ropes.forEach {
            RopeComponent(it)
        }

        notes.forEachIndexed { index, n ->
            NoteCardComponent(
                note = n,
                onPositionChanged = { noteId, x, y ->
                    onDragNote(noteId, x, y)
                },
                onGetSize = { size ->
                    onGetSize(size, index)
                },
                onSelect = { t, off ->
                    if (isSelectionMode || isConnectionMode) {
                        onSelectedCard(t.noteId)
                    } else {
                        selectedNote = t
                        popupOffset = off
                        showMenu = true
                    }
                },
                isSelectionMode = isSelectionMode,
                isConnectionMode = isConnectionMode
            )
        }

        if (showMenu) {
            Popup(
                offset = IntOffset(popupOffset.x.roundToInt(), popupOffset.y.roundToInt()),
                onDismissRequest = { showMenu = false },
                content = {
                    Column(modifier = Modifier.widthIn(max = 150.dp)) {
                        OverflowMenuItem(
                            title = "Connect Note",
                            onClick = {
                                onConnectCard(selectedNote!!)
                                showMenu = false
                            }
                        )
                        OverflowMenuItem(
                            title = "Update Note",
                            onClick = {
                                val note = notes.first { it.noteId == selectedNote?.noteId }
                                onUpdateNote(note)
                                showMenu = false
                            }
                        )
                    }
                },
            )
        }
    }
}