package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.gestures.TransformableState
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
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.DismissDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnGetBoardSize
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnImportBooks
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnImportQuotes
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenBookDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenNewBoardDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenQuoteDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnSaveBoard
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@Composable
fun BoardEditorScreen(
    viewModel: BoardEditorViewModel = hiltViewModel(),
    topAppBarTitle: String,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        viewModel.onAction(BoardEditorAction.OnTransformChange(zoomChange, offsetChange))
    }

    BoardEditorContent(
        scaffoldState = scaffoldState,
        title = if (uiState.selectedNoteIds.isNotEmpty()) "${uiState.selectedNoteIds.size} Selected" else (uiState.board?.name
            ?: topAppBarTitle),
        uiState = uiState,
        onAction = viewModel::onAction,
        state = state,
        onBack = onBack
    )

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
                        viewModel.addNote(
                            title = content,
                            image = "",
                            color = color
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
fun BoardEditorContent(
    scaffoldState: ScaffoldState,
    title: String,
    uiState: BoardEditorState,
    onAction: (BoardEditorAction) -> Unit,
    state: TransformableState,
    onBack: () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = title,
                backButton = {
                    IconButton(
                        onClick = {
                            if (uiState.selectedNoteIds.isNotEmpty()) {
                                onAction(BoardEditorAction.OnResetSelectedNotes)
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
                            onClick = { onAction(BoardEditorAction.OnDeleteSelectedNotes) },
                            content = { Icon(PhosphorTrash, null) },
                        )
                    } else {
                        IconButton(
                            onClick = {
                                if (uiState.board == null) {
                                    onAction(OnOpenNewBoardDialog)
                                } else {
                                    onAction(
                                        OnSaveBoard(uiState.board.name, uiState.board.color)
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
                    onAction(BoardEditorAction.OnOpenAddNoteDialog(n, false))
                },
                onAddNote = {
                    onAction(BoardEditorAction.OnOpenAddNoteDialog(null, false))
                },
                onSaveImage = { path, color ->
                    onAction(BoardEditorAction.OnAddNote("", path, color))
                },
                onTidyUp = { onAction(BoardEditorAction.OnTidyUpNotes) },
                onToggleGrid = { onAction(BoardEditorAction.OnToggleGrid) },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onSizeChanged { onAction(OnGetBoardSize(it)) }
                .transformable(state)) {
            if (uiState.showGrid) {
                DotBackgroundComponent(scale = uiState.camera.zoom, offset = uiState.camera.offset)
            }

            BoardEditor(
                notes = uiState.notes.filter { it.status == "active" },
                ropes = uiState.ropes.filter { it.status == "active" },
                onDragNote = { note, current ->
                    onAction(BoardEditorAction.OnDragNote(note, current))
                },
                scale = uiState.camera.zoom,
                offset = uiState.camera.offset,
                onSelectedCard = { onAction(BoardEditorAction.OnSelectNote(it)) },
                onGetSize = { size, index ->
                    onAction(BoardEditorAction.OnGetNoteSize(size, index))
                },
                onAddQuickNote = {
                    onAction(
                        BoardEditorAction.OnAddNote(
                            title = "",
                            image = "",
                            color = colors[0],
                            posX = (it.x - uiState.camera.offset.x) / uiState.camera.zoom,
                            posY = (it.y - uiState.camera.offset.y) / uiState.camera.zoom,
                            isQuickAdd = true
                        )
                    )
                },
                onDragEnd = { onAction(BoardEditorAction.OnDragEnd) },
                noteHighlightedId = uiState.noteHighlightId,
                previewRope = uiState.previewRope
            )

            BoardTools(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onZoomIn = { onAction(BoardEditorAction.OnChangeCameraZoom(true)) },
                onZoomOut = { onAction(BoardEditorAction.OnChangeCameraZoom(false)) },
                onResetZoom = { onAction(BoardEditorAction.OnResetCamera) },
                scale = uiState.camera.zoom,
                onImportQuotes = { onAction(OnOpenQuoteDialog) },
                onImportBooks = { onAction(OnOpenBookDialog) },
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