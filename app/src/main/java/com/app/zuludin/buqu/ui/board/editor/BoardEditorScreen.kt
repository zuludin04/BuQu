package com.app.zuludin.buqu.ui.board.editor

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.TextSelectionDialog
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorCheck
import com.app.zuludin.buqu.core.icons.PhosphorDotsThreeVertical
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorLineSegments
import com.app.zuludin.buqu.core.icons.PhosphorLinkBreak
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.icons.PhosphorMinus
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.core.icons.PhosphorSelectionAll
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.icons.PhosphorXCircle
import com.app.zuludin.buqu.core.utils.SpeechRecognizerContract
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import java.util.Objects
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
    var showOverflowMenu by remember { mutableStateOf(false) }
    var overflowMenuPosition by remember { mutableStateOf(Offset.Zero) }
    var showBoardNameDialog by remember { mutableStateOf(false) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    var noteText by remember { mutableStateOf("") }

    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = SpeechRecognizerContract(), onResult = {
            val result = it.toString()
            noteText = result.substring(1, result.length - 1)
            showAddNoteSheet = true
        })

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showTextSelection by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val file = remember { context.createImageFile() }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedBitmap = context.fixImageRotation(uri)
                showTextSelection = true
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { success ->
            if (success != null) {
                capturedBitmap = context.fixImageRotation(success)
                showTextSelection = true
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    if (showTextSelection && capturedBitmap != null) {
        TextSelectionDialog(
            bitmap = capturedBitmap!!,
            onDismiss = { showTextSelection = !showTextSelection },
            onTextSelected = { selectedText ->
                noteText = selectedText
                showTextSelection = !showTextSelection
                showAddNoteSheet = true
            }
        )
    }

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
                                if (uiState.isSelectionMode) PhosphorX else PhosphorArrowLeft,
                                null
                            )
                        }
                    )
                },
                actions = {
                    if (uiState.isSelectionMode) {
                        IconButton(
                            onClick = { viewModel.deleteSelectedNotes() },
                            content = { Icon(PhosphorTrash, null) }
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
                            content = { Icon(PhosphorCheck, null) }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        content = { Icon(PhosphorAperture, null) }
                    )
                    IconButton(
                        onClick = {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        content = { Icon(PhosphorImage, null) }
                    )
                    IconButton(
                        onClick = { speechRecognizerLauncher.launch(Unit) },
                        content = { Icon(PhosphorMicrophone, null) }
                    )
                    IconButton(
                        modifier = Modifier.onGloballyPositioned {
                            overflowMenuPosition = it.positionOnScreen()
                        },
                        onClick = {
                            showOverflowMenu = true
                        },
                        content = { Icon(PhosphorDotsThreeVertical, null) }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        content = { Icon(PhosphorPlus, null) },
                        onClick = { showAddNoteSheet = true },
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if (uiState.isConnectionMode || uiState.isSelectionMode) 4.dp else 0.dp,
                    color = if (uiState.isConnectionMode)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else if (uiState.isSelectionMode)
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                    else Color.Transparent
                )
        ) {
            GridBackground(scale = scale, offset = offset)

            BoardEditor(
                modifier = Modifier.padding(paddingValues),
                notes = uiState.notes,
                yarns = uiState.ropes,
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

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .padding(bottom = 100.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                tonalElevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    IconButton(onClick = { scale = (scale - 0.1f).coerceAtLeast(0.5f) }) {
                        Icon(PhosphorMinus, null, modifier = Modifier.size(18.dp))
                    }
                    Text(
                        text = "${(scale * 100).roundToInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .width(40.dp)
                            .clickable {
                                scale = 1f
                                offset = Offset.Zero
                            },
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { scale = (scale + 0.1f).coerceAtMost(3f) }) {
                        Icon(PhosphorPlus, null, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Selection/Connection Mode Indicator & Hint
            if (uiState.isConnectionMode || uiState.isSelectionMode) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = if (uiState.isConnectionMode)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 6.dp
                    ) {
                        Text(
                            text = if (uiState.isConnectionMode) "Connection Mode" else "Selection Mode",
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (uiState.isConnectionMode)
                                "Tap two cards to link them"
                            else "Tap cards to select/deselect",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
            ) {
                IconButton(
                    modifier = Modifier.background(
                        color = if (uiState.isConnectionMode) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.9f
                        ) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                    ),
                    onClick = { viewModel.toggleConnectionMode() },
                    content = {
                        Icon(
                            imageVector = PhosphorLineSegments,
                            contentDescription = null,
                            tint = if (uiState.isConnectionMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                )
                IconButton(
                    modifier = Modifier.background(
                        color = if (uiState.isSelectionMode) MaterialTheme.colorScheme.tertiary.copy(
                            alpha = 0.9f
                        ) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                    ),
                    onClick = { viewModel.toggleSelectionModel() },
                    content = {
                        Icon(
                            imageVector = PhosphorSelectionAll,
                            contentDescription = null,
                            tint = if (uiState.isSelectionMode) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
        TheoryBottomDialog(
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
                viewModel.addNote(content, color)
                showAddNoteSheet = !showAddNoteSheet
            }
        )
    }

    if (showOverflowMenu) {
        Popup(
            offset = IntOffset(
                overflowMenuPosition.x.roundToInt() + 80,
                overflowMenuPosition.y.roundToInt() - 180
            ),
            onDismissRequest = { showOverflowMenu = !showOverflowMenu }
        ) {
            Column(modifier = Modifier.widthIn(max = 120.dp)) {
                Surface(
                    color = Color.White,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.clickable {
                            showOverflowMenu = !showOverflowMenu
                        }
                    ) {
                        Text(
                            "Auto Layout",
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
                Surface(
                    color = Color.White,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.clickable {
                            showOverflowMenu = false
                        }
                    ) {
                        Text(
                            "Settings",
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoardNameDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val colors = listOf(
        "E1F5FE", "FFF9C4", "F1F8E9",
        "FFEBEE", "F3E5F5", "EFEBE9"
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Board") },
        text = {
            Column {
                Text("Please enter a name and pick a color for this board.")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Board Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text("Board Theme", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color("#${color}".toColorInt()))
                                .border(
                                    width = if (selectedColor == color) 3.dp else 1.dp,
                                    color = if (selectedColor == color)
                                        MaterialTheme.colorScheme.primary
                                    else Color.LightGray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name, selectedColor) },
                enabled = name.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputDialog(
    inputText: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var text by remember { mutableStateOf(inputText) }
    val colors = listOf(
        "E1F5FE", "FFF9C4", "F1F8E9",
        "FFEBEE", "F3E5F5", "EFEBE9"
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Add New Card",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 5,
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "" }) {
                            Icon(PhosphorXCircle, null)
                        }
                    }
                }
            )

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputHelperChip(
                    label = "Voice",
                    icon = PhosphorMicrophone,
                    onClick = { }
                )

                InputHelperChip(
                    label = "Scan",
                    icon = PhosphorAperture,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Color", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color("#${color}".toColorInt()))
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color)
                                    MaterialTheme.colorScheme.primary
                                else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text, selectedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = text.isNotBlank()
            ) {
                Text("Create Card", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun InputHelperChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun BoardEditor(
    modifier: Modifier = Modifier,
    notes: List<NoteCard>,
    yarns: List<Rope>,
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
        if (yarns.isNotEmpty()) {
            yarns.forEach {
                DraggableYarn(
                    initialRope = Offset(it.sourceX, it.sourceY),
                    targetRope = Offset(it.targetX, it.targetY),
                    sourceSize = it.sourceSize,
                    targetSize = it.targetSize,
                )
            }
        }

        notes.forEachIndexed { index, n ->
            DraggableCard(
                note = n,
                onPositionChanged = { noteId, x, y ->
                    onDragNote(noteId, x, y)
                },
                onGetSize = { size ->
                    onGetSize(size, index)
                },
                onSelect = { t, off ->
                    Log.d("CARD_SIZE", t.size.toString())
                    if (isSelectionMode || isConnectionMode) {
                        onSelectedCard(t.noteId)
                    } else {
                        selectedNote = t
                        popupOffset = off
                        showMenu = true
                    }
                },
                isSelectionMode = isSelectionMode,
                isSelected = n.isSelected,
                isConnectionMode = isConnectionMode
            )
        }

        if (showMenu) {
            Popup(
                offset = IntOffset(popupOffset.x.roundToInt(), popupOffset.y.roundToInt()),
                onDismissRequest = { showMenu = false },
                content = {
                    Surface(
                        modifier = Modifier.size(120.dp, 80.dp),
                        color = Color.White,
                        tonalElevation = 8.dp,
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                onConnectCard(selectedNote!!)
                                showMenu = false
                            }
                        ) {
                            Text("Connect Note", Modifier.align(Alignment.Center))
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryBottomDialog(
    source: NoteCard,
    notes: List<NoteCard>,
    onDismiss: (NoteCard?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val potentialTargets = notes.filter { it.noteId != source.noteId }

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Connect to...",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Linking from: \"${source.title.take(20)}${if (source.title.length > 20) "..." else ""}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (potentialTargets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No other cards to connect to.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(potentialTargets) { target ->
                        val noteColor = Color("#${target.color}".toColorInt())
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(noteColor.copy(alpha = 0.3f))
                                .border(1.dp, noteColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    onDismiss(target)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(noteColor)
                                    .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = target.title,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = PhosphorLinkBreak,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DraggableYarn(
    initialRope: Offset,
    targetRope: Offset,
    sourceSize: IntSize,
    targetSize: IntSize
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val startCenterOffset =
            Offset(
                sourceSize.width.pxToDp().toPx() / 2,
                sourceSize.height.pxToDp().toPx() / 2
            )
        val targetCenterOffset =
            Offset(
                targetSize.width.pxToDp().toPx() / 2,
                targetSize.height.pxToDp().toPx() / 2
            )

        drawLine(
            color = Color(0xFF7D5260),
            start = initialRope + startCenterOffset,
            end = targetRope + targetCenterOffset,
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun DraggableCard(
    note: NoteCard,
    onPositionChanged: (String, Float, Float) -> Unit,
    onSelect: (NoteCard, Offset) -> Unit,
    onGetSize: (IntSize) -> Unit,
    isDraggable: Boolean = true,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    isConnectionMode: Boolean
) {
    var isDragging by remember { mutableStateOf(false) }
    var newOffset by remember { mutableStateOf(Offset(note.posX, note.posY)) }

    val updatedOnPositionChanged by rememberUpdatedState(onPositionChanged)

    Box(
        modifier = Modifier
            .widthIn(max = 180.dp)
            .onSizeChanged { onGetSize(it) }
            .offset {
                IntOffset(
                    note.posX.roundToInt(),
                    note.posY.roundToInt()
                )
            }
            .graphicsLayer {
                // Add a slight rotation based on the noteId for an organic "sticky note" feel
                rotationZ = (note.noteId.hashCode() % 6 - 3).toFloat()

                if (isDraggable) {
                    val scaleValue =
                        if (isDragging || (isSelectionMode && isSelected)) 1.15f else 1f
                    scaleX = scaleValue
                    scaleY = scaleValue
                }
            }
            .neumorphicShadow(backgroundColor = Color("#${note.color}".toColorInt()))
            .border(
                width = if (isSelectionMode && isSelected) 3.dp else 0.dp,
                color = if (isSelectionMode && isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .pointerInput(note.noteId, isSelectionMode, isConnectionMode) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val absoluteTapPos = Offset(
                            newOffset.x + tapOffset.x,
                            newOffset.y + tapOffset.y
                        )
                        onSelect(note, absoluteTapPos)
                        isDragging = false
                    }
                )
            }
            .pointerInput(note.noteId, isSelectionMode, isConnectionMode) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        if (!isSelectionMode && !isConnectionMode) {
                            change.consume()
                            newOffset += dragAmount
                            updatedOnPositionChanged(note.noteId, newOffset.x, newOffset.y)
                        }
                    }
                )
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Visual "Tape" or "Pin" element
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(12.dp)
                    .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GridBackground(scale: Float, offset: Offset) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gridSize = 40.dp.toPx()
        val scaledGridSize = gridSize * scale

        // Calculate start positions based on offset to keep grid consistent during pan
        val startX = (offset.x % scaledGridSize)
        val startY = (offset.y % scaledGridSize)

        for (x in startX.toInt()..size.width.toInt() step scaledGridSize.toInt()) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.2f),
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
        for (y in startY.toInt()..size.height.toInt() step scaledGridSize.toInt()) {
            drawLine(
                color = Color.LightGray.copy(alpha = 0.2f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
    }
}