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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.app.zuludin.buqu.core.icons.PhosphorMagnifyingGlass
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.core.icons.PhosphorSelectionAll
import com.app.zuludin.buqu.core.icons.PhosphorTrash
import com.app.zuludin.buqu.core.icons.PhosphorX
import com.app.zuludin.buqu.core.icons.PhosphorXCircle
import com.app.zuludin.buqu.core.utils.SpeechRecognizerContract
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import com.app.zuludin.buqu.core.utils.pxToDp
import com.app.zuludin.buqu.domain.models.Note
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Yarn
import java.text.DecimalFormat
import java.util.Objects
import java.util.UUID
import kotlin.math.roundToInt

@Composable
fun BoardEditorScreen(
    viewModel: BoardEditorViewModel = hiltViewModel(),
    topAppBarTitle: String,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val cards = remember { mutableStateListOf<Note>() }
    val yarns = remember { mutableStateListOf<Yarn>() }
    val selectedNoteIds = remember { mutableStateListOf<String>() }
    var showConnectionSheet by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var sourceTheory by remember { mutableStateOf<Note?>(null) }
    var overflowMenuPosition by remember { mutableStateOf(Offset.Zero) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    var noteText by remember { mutableStateOf("") }
    var isSelectionMode by remember { mutableStateOf(false) }

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

    var isConnectionMode by remember { mutableStateOf(false) }
    var sourceConnectionNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = if (isSelectionMode) "${selectedNoteIds.size} Selected" else topAppBarTitle,
                backButton = {
                    IconButton(
                        onClick = {
                            if (isSelectionMode) {
                                isSelectionMode = false
                                selectedNoteIds.clear()
                            } else {
                                onBack()
                            }
                        },
                        content = {
                            Icon(
                                if (isSelectionMode) PhosphorX else PhosphorArrowLeft,
                                null
                            )
                        }
                    )
                },
                actions = {
                    if (isSelectionMode && selectedNoteIds.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                cards.removeAll { note ->
                                    selectedNoteIds.contains(note.id)
                                }
                                selectedNoteIds.clear()
                            },
                            content = { Icon(PhosphorTrash, null) }
                        )
                    } else {
                        IconButton(
                            onClick = { viewModel.saveBoarAndCards("Hallo Board") },
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
        Box {
            BoardEditor(
                modifier = Modifier.padding(paddingValues),
                notes = uiState.notes,
                yarns = yarns,
                onDragNote = { noteId, x, y ->
                    viewModel.dragNoteCard(noteId, x, y)
                },
                onSelectTheory = {
                    sourceTheory = it
                    showConnectionSheet = true
                },
                scale = scale,
                offset = offset,
                state = state,
                isSelectionMode = isSelectionMode,
                onSelectedCard = { index, id ->
//                    val currentCard = cards[index]
//                    val isSelected = !currentCard.isSelected
//                    val card = currentCard.copy(isSelected = isSelected)
//                    cards[index] = card
//
//                    if (selectedNoteIds.contains(id)) {
//                        selectedNoteIds.remove(id)
//                    } else {
//                        selectedNoteIds.add(id)
//                    }
                    Log.d("NOTE_SIZE", uiState.notes[index].size.toString())
                },
                isConnectionMode = isConnectionMode,
                onConnectCard = { note ->
                    if (sourceConnectionNote == null) {
                        sourceConnectionNote = note
                    } else if (sourceConnectionNote?.id != note.id) {
                        val yarn = Yarn(
                            id = UUID.randomUUID().toString(),
                            sourceNoteId = sourceConnectionNote!!.id,
                            targetNoteId = note.id,
                            xSource = sourceConnectionNote!!.xPos,
                            ySource = sourceConnectionNote!!.yPos,
                            xTarget = note.xPos,
                            yTarget = note.yPos,
                            sourceSize = sourceConnectionNote!!.size,
                            targetSize = note.size
                        )
                        yarns.add(yarn)
                        sourceConnectionNote = null
                    }
                },
                onGetSize = { size, index ->
                    viewModel.getCardSize(size, index)
                }
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                IconButton(
                    onClick = { isConnectionMode = !isConnectionMode },
                    content = {
                        Icon(
                            imageVector = PhosphorLineSegments,
                            contentDescription = null,
                            tint = if (isConnectionMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                )
                IconButton(
                    onClick = { isSelectionMode = !isSelectionMode },
                    content = { Icon(PhosphorSelectionAll, null) }
                )
            }

            Row {
                Text("x${DecimalFormat("#.#").format(scale)}")
                IconButton(
                    onClick = {
                        scale = 1f
                        offset = Offset.Zero
                    },
                    content = { Icon(PhosphorMagnifyingGlass, null) }
                )
            }
        }
    }

    if (showConnectionSheet) {
        TheoryBottomDialog(
            source = sourceTheory!!,
            theories = cards,
            onDismiss = {
                showConnectionSheet = !showConnectionSheet
                if (it != null) {
                    yarns.add(it)
                }
            }
        )
    }

    if (showAddNoteSheet) {
        NoteInputDialog(
            onDismiss = { showAddNoteSheet = !showAddNoteSheet },
            inputText = noteText,
            onConfirm = { content, _ ->
                viewModel.addNote(content)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputDialog(
    inputText: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Color) -> Unit
) {
    var text by remember { mutableStateOf(inputText) }
    val colors = listOf(
        Color(0xFFE1F5FE), Color(0xFFFFF9C4), Color(0xFFF1F8E9),
        Color(0xFFFFEBEE), Color(0xFFF3E5F5), Color(0xFFEFEBE9)
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
                            .background(color)
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
    yarns: MutableList<Yarn>,
    onDragNote: (String, Float, Float) -> Unit,
    onGetSize: (IntSize, Int) -> Unit,
    onSelectTheory: (Note) -> Unit,
    scale: Float,
    offset: Offset,
    state: TransformableState,
    isSelectionMode: Boolean,
    isConnectionMode: Boolean,
    onConnectCard: (Note) -> Unit,
    onSelectedCard: (Int, String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedTheory by remember { mutableStateOf<Note?>(null) }

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
        yarns.forEach {
            DraggableYarn(
                initialRope = Offset(it.xSource, it.ySource),
                targetRope = Offset(it.xTarget, it.yTarget),
                targetTheory = it.targetSize,
                startTheory = it.sourceSize,
            )
        }

        notes.forEachIndexed { index, n ->
            DraggableCard(
                note = n,
                onPositionChanged = { noteId, x, y ->
//                    if (yarns.isNotEmpty()) {
//                        val sourceRope = yarns.filter { it.sourceNoteId == note.id }
//                        if (sourceRope.isNotEmpty()) {
//                            sourceRope.forEach { rope ->
//                                val r = rope.copy(xSource = note.xPos, ySource = note.yPos)
//                                val selected = yarns.first { it.id == rope.id }
//                                val index = yarns.indexOf(selected)
//                                yarns[index] = r
//                            }
//                        }
//
//                        val targetRope = yarns.filter { it.targetNoteId == note.id }
//                        if (targetRope.isNotEmpty()) {
//                            targetRope.forEach { rope ->
//                                val r = rope.copy(xTarget = note.xPos, yTarget = note.yPos)
//                                val selected = yarns.first { it.id == rope.id }
//                                val index = yarns.indexOf(selected)
//                                yarns[index] = r
//                            }
//                        }
//                    }
//
//                    onDragNote(note, index)
                    onDragNote(noteId, x, y)
                },
                onGetSize = { size ->
                    onGetSize(size, index)
                },
                onSelect = { t, off ->
//                    if (isSelectionMode) {
                    onSelectedCard(index, n.noteId)
//                    } else if (isConnectionMode) {
//                        onConnectCard(t)
//                    } else {
//                        selectedTheory = t
//                        popupOffset = off
//                        showMenu = true
//                    }
                },
                isSelectionMode = isSelectionMode,
                isSelected = /*n.isSelected*/ false,
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
                                onSelectTheory(selectedTheory!!)
                                showMenu = false
                            }
                        ) {
                            Text("Connect Theory", Modifier.align(Alignment.Center))
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
    source: Note,
    theories: List<Note>,
    onDismiss: (Yarn?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val potentialTargets = theories.filter { it.id != source.id }

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
                text = "Linking from: \"${source.content.take(20)}${if (source.content.length > 20) "..." else ""}\"",
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(target.color.copy(alpha = 0.3f))
                                .border(1.dp, target.color, RoundedCornerShape(16.dp))
                                .clickable {
                                    val yarn = Yarn(
                                        id = UUID.randomUUID().toString(),
                                        sourceNoteId = source.id,
                                        targetNoteId = target.id,
                                        xSource = source.xPos,
                                        ySource = source.yPos,
                                        xTarget = target.xPos,
                                        yTarget = target.yPos,
                                        sourceSize = source.size,
                                        targetSize = target.size
                                    )
                                    onDismiss(yarn)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(target.color)
                                    .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = target.content,
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
    startTheory: IntSize,
    targetTheory: IntSize
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val startCenterOffset =
            Offset(
                startTheory.width.pxToDp().toPx() / 2,
                startTheory.height.pxToDp().toPx() / 2
            )
        val targetCenterOffset =
            Offset(
                targetTheory.width.pxToDp().toPx() / 2,
                targetTheory.height.pxToDp().toPx() / 2
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
    var newTheory by remember { mutableStateOf<Note?>(null) }

    val updatedOnPositionChanged by rememberUpdatedState(onPositionChanged)

    Box(
        modifier = Modifier
            .onSizeChanged { onGetSize(it) }
            .offset {
                IntOffset(
                    note.posX.roundToInt(),
                    note.posY.roundToInt()
                )
            }
            .graphicsLayer {
                if (isDraggable) {
                    val scaleValue = if (isDragging || (isSelectionMode && isSelected)) 1.1f else 1f
                    scaleX = scaleValue
                    scaleY = scaleValue
                }
            }
            .neumorphicShadow(backgroundColor = Color.LightGray)
            .border(
                width = if (isSelectionMode && isSelected) 3.dp else 0.dp,
                color = if (isSelectionMode && isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .pointerInput(note.noteId, isSelectionMode, isConnectionMode) {
                detectTapGestures(
                    onTap = { tapOffset ->
//                        val absoluteTapPos = Offset(
//                            newOffset.x + tapOffset.x,
//                            newOffset.y + tapOffset.y
//                        )
//                        newTheory = note.copy(xPos = newOffset.x, yPos = newOffset.y)
//                        onSelect(newTheory!!, absoluteTapPos)
//                        isDragging = false
                        onSelect(note, tapOffset)
                    }
                )
            }
            .pointerInput(note.noteId) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        newOffset += dragAmount
                        updatedOnPositionChanged(note.noteId, newOffset.x, newOffset.y)
                    }
                )
            }
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = note.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}