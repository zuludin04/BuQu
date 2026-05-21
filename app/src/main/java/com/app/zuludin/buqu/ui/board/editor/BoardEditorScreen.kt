package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.ConfirmInputNote
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.ConfirmUpsertBoard
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.DeleteBoard
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.DragNote
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnCheckBoard
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnDeleteSelectedNotes
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnGetBoardSize
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnGetNoteSize
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnOpenDialog
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnResetSelectedNotes
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnSelectNote
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnTidyUpNotes
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.OnToggleGrid
import com.app.zuludin.buqu.ui.board.editor.BoardEditorAction.TransformCamera
import com.app.zuludin.buqu.ui.board.editor.component.BackgroundType
import com.app.zuludin.buqu.ui.board.editor.component.BoardInfiniteCanvas
import com.app.zuludin.buqu.ui.board.editor.component.BottomBarEditor
import com.app.zuludin.buqu.ui.board.editor.component.NoteCardComponent
import com.app.zuludin.buqu.ui.board.editor.component.RopeComponent
import com.app.zuludin.buqu.ui.board.editor.dialog.BookImportDialog
import com.app.zuludin.buqu.ui.board.editor.dialog.NoteInputDialog
import com.app.zuludin.buqu.ui.board.editor.dialog.QuoteImportDialog
import com.app.zuludin.buqu.ui.board.editor.dialog.SaveBoardDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BoardEditorScreen(
    viewModel: BoardEditorViewModel = hiltViewModel(),
    topAppBarTitle: String,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                BoardEditorEvent.SuccessSaveBoard -> {
                    scaffoldState.snackbarHostState.showSnackbar("Your board is saved")
                }

                BoardEditorEvent.GoHome -> onBack()

                is BoardEditorEvent.CreateConnectedRope -> {
                    scope.launch {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Create rope to connect two notes?",
                            actionLabel = "Confirm",
                            duration = SnackbarDuration.Long
                        )

                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.createConnectedRope(event.rope)
                            }

                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }
            }
        }
    }

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
                                viewModel.onAction(OnResetSelectedNotes)
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
                            onClick = { viewModel.onAction(OnDeleteSelectedNotes) },
                            content = { Icon(PhosphorTrash, null) },
                        )
                    } else {
                        IconButton(
                            onClick = { viewModel.onAction(OnCheckBoard) },
                            content = { Icon(PhosphorCheck, null) },
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomBarEditor(
                onTextResult = { text ->
                    viewModel.onAction(
                        OnOpenDialog(
                            BoardDialogState.InputNoteDialog(
                                null,
                                text,
                                ""
                            )
                        )
                    )
                },
                onAddNote = {
                    viewModel.onAction(
                        OnOpenDialog(
                            BoardDialogState.InputNoteDialog(
                                null,
                                "",
                                ""
                            )
                        )
                    )
                },
                onSaveImage = { path, color ->
                    viewModel.onAction(ConfirmInputNote(null, "", path, color))
                },
                onTidyUp = { viewModel.onAction(OnTidyUpNotes) },
                onToggleGrid = { viewModel.onAction(OnToggleGrid) },
                showDelete = uiState.board != null,
                onDeleteBoard = { viewModel.onAction(DeleteBoard) }
            )
        },
    ) { paddingValues ->
        BoardEditorContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            uiState = uiState,
            onAction = viewModel::onAction
        )
    }

    when (val dialog = uiState.dialogState) {
        BoardDialogState.None -> Unit
        is BoardDialogState.InputNoteDialog -> NoteInputDialog(
            noteId = dialog.noteId,
            title = dialog.title,
            color = dialog.color,
            onDismiss = { viewModel.onAction(OnOpenDialog(BoardDialogState.None)) },
            onConfirm = { noteId, text, color ->
                viewModel.onAction(ConfirmInputNote(noteId, text, "", color))
            },
        )

        is BoardDialogState.UpsertBoardDialog -> SaveBoardDialog(
            onDismiss = { viewModel.onAction(OnOpenDialog(BoardDialogState.None)) },
            onConfirm = { name, color ->
                viewModel.onAction(ConfirmUpsertBoard(name, color))
            },
        )

        BoardDialogState.ImportBooks -> BookImportDialog(
            books = uiState.books,
            onDismiss = { viewModel.onAction(OnOpenDialog(BoardDialogState.None)) },
            onImportBooks = { viewModel.onAction(BoardEditorAction.ConfirmImportBooks(it)) },
        )

        BoardDialogState.ImportQuotes -> QuoteImportDialog(
            quotes = uiState.quotes,
            onDismiss = { viewModel.onAction(OnOpenDialog(BoardDialogState.None)) },
            onImportQuotes = { viewModel.onAction(BoardEditorAction.ConfirmImportQuotes(it)) },
        )
    }
}

@Composable
private fun BoardEditorContent(
    modifier: Modifier,
    uiState: BoardEditorState,
    onAction: (BoardEditorAction) -> Unit
) {
    BoardInfiniteCanvas(
        modifier = modifier,
        camera = uiState.camera,
        onCameraChange = { onAction(TransformCamera(it.offset, it.zoom)) },
        backgroundType = BackgroundType.Line,
        onGetBoardSize = { onAction(OnGetBoardSize(it)) },
        openDialog = { onAction(OnOpenDialog(it)) },
        showGrid = uiState.showGrid,
    ) {
        uiState.ropes.filter { r -> r.status == "active" }.forEach { rope ->
            RopeComponent(rope, false)
        }

        if (uiState.previewRope != null) RopeComponent(uiState.previewRope, true)

        uiState.notes.filter { note -> note.status == "active" }.forEachIndexed { index, n ->
            NoteCardComponent(
                note = n,
                onPositionChanged = { pos ->
                    onAction(DragNote(n, pos))
                },
                onGetSize = { size -> onAction(OnGetNoteSize(size, index)) },
                onSelect = { noteId -> onAction(OnSelectNote(noteId)) },
                onDragEnd = { onAction(BoardEditorAction.OnDragEnd) },
                onUpdateNote = { note ->
                    onAction(
                        OnOpenDialog(
                            BoardDialogState.InputNoteDialog(
                                note.noteId,
                                note.title,
                                note.color
                            )
                        )
                    )
                },
                isHighlighted = uiState.noteHighlightId == n.noteId,
                scale = it.zoom,
            )
        }
    }
}