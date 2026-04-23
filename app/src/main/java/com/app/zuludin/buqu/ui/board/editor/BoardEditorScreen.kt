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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorNote
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import kotlin.math.roundToInt

@Composable
fun BoardEditorScreen(
    viewModel: BoardEditorViewModel = hiltViewModel(),
    topAppBarTitle: String,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showConnectionSheet by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }
    var showBoardNameDialog by remember { mutableStateOf(false) }
    var showImportQuotesDialog by remember { mutableStateOf(false) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    var noteText by remember { mutableStateOf("") }

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
                    noteText = text
                    showAddNoteSheet = true
                },
                onAddNote = { showAddNoteSheet = true },
                onSaveImage = { path, color -> viewModel.addNote("", path, color) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
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
            onDismiss = { showAddNoteSheet = !showAddNoteSheet },
            inputText = noteText,
            onConfirm = { content, color ->
                viewModel.addNote(content, "", color)
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
    onSelectedCard: (String) -> Unit
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
                    }
                },
            )
        }
    }
}